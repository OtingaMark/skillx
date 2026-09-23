const { onCall, onRequest, HttpsError } = require("firebase-functions/v2/https");
const { defineString } = require("firebase-functions/params");
const admin = require("firebase-admin");
const axios = require("axios");

admin.initializeApp();
const db = admin.firestore();

const CONSUMER_KEY = defineString("DARAJA_CONSUMER_KEY");
const CONSUMER_SECRET = defineString("DARAJA_CONSUMER_SECRET");
const SHORTCODE = defineString("DARAJA_SHORTCODE");
const PASSKEY = defineString("DARAJA_PASSKEY");
const CALLBACK_URL = defineString("DARAJA_CALLBACK_URL");
const ENVIRONMENT = defineString("DARAJA_ENVIRONMENT", { default: "sandbox" });

function baseUrl() {
  return ENVIRONMENT.value() === "production"
    ? "https://api.safaricom.co.ke"
    : "https://sandbox.safaricom.co.ke";
}

function normalizePhone(phone) {
  let p = String(phone || "").replace(/\s+/g, "");
  if (p.startsWith("07")) p = "254" + p.substring(1);
  if (p.startsWith("01")) p = "254" + p.substring(1);
  if (p.startsWith("+254")) p = p.substring(1);
  return p;
}

async function accessToken() {
  const auth = Buffer.from(`${CONSUMER_KEY.value()}:${CONSUMER_SECRET.value()}`).toString("base64");
  const response = await axios.get(`${baseUrl()}/oauth/v1/generate?grant_type=client_credentials`, {
    headers: { Authorization: `Basic ${auth}` }
  });
  return response.data.access_token;
}

function timestamp() {
  const d = new Date();
  const pad = n => String(n).padStart(2, "0");
  return `${d.getFullYear()}${pad(d.getMonth()+1)}${pad(d.getDate())}${pad(d.getHours())}${pad(d.getMinutes())}${pad(d.getSeconds())}`;
}

exports.startMpesaStkPush = onCall({ region: "africa-south1" }, async request => {
  if (!request.auth) throw new HttpsError("unauthenticated", "Sign in first.");
  const phone = normalizePhone(request.data?.phone);
  const amount = Number(request.data?.amount);
  if (!/^2547\d{8}$/.test(phone)) throw new HttpsError("invalid-argument", "Enter a valid Kenyan M-Pesa number.");
  if (!Number.isInteger(amount) || amount < 10 || amount > 150000) throw new HttpsError("invalid-argument", "Amount must be between KES 10 and KES 150,000.");

  const ts = timestamp();
  const password = Buffer.from(`${SHORTCODE.value()}${PASSKEY.value()}${ts}`).toString("base64");
  const token = await accessToken();
  const response = await axios.post(`${baseUrl()}/mpesa/stkpush/v1/processrequest`, {
    BusinessShortCode: SHORTCODE.value(),
    Password: password,
    Timestamp: ts,
    TransactionType: "CustomerPayBillOnline",
    Amount: amount,
    PartyA: phone,
    PartyB: SHORTCODE.value(),
    PhoneNumber: phone,
    CallBackURL: CALLBACK_URL.value(),
    AccountReference: `SKILLX-${request.auth.uid.substring(0, 8)}`,
    TransactionDesc: "SkillX point top-up"
  }, { headers: { Authorization: `Bearer ${token}` } });

  const checkoutId = response.data.CheckoutRequestID;
  await db.collection("mpesaTopups").doc(checkoutId).set({
    userId: request.auth.uid,
    phone,
    amount,
    points: Math.floor(amount / 10),
    status: "pending",
    checkoutRequestId: checkoutId,
    createdAt: admin.firestore.FieldValue.serverTimestamp()
  });
  return { message: "M-Pesa prompt sent. Complete the payment on your phone.", checkoutRequestId: checkoutId };
});

exports.mpesaCallback = onRequest({ region: "africa-south1" }, async (req, res) => {
  try {
    const body = req.body?.Body?.stkCallback;
    if (!body) return res.json({ ResultCode: 0, ResultDesc: "Accepted" });
    const checkoutId = body.CheckoutRequestID;
    const ref = db.collection("mpesaTopups").doc(checkoutId);
    const topup = await ref.get();
    if (!topup.exists) return res.json({ ResultCode: 0, ResultDesc: "Accepted" });

    if (body.ResultCode === 0) {
      const data = topup.data();
      const userRef = db.collection("users").doc(data.userId);
      await db.runTransaction(async tx => {
        const current = await tx.get(ref);
        if (current.get("status") === "completed") return;
        tx.update(ref, {
          status: "completed",
          mpesaReceipt: body.CallbackMetadata?.Item?.find(x => x.Name === "MpesaReceiptNumber")?.Value || null,
          completedAt: admin.firestore.FieldValue.serverTimestamp()
        });
        tx.update(userRef, { points: admin.firestore.FieldValue.increment(data.points) });
      });
    } else {
      await ref.update({ status: "failed", resultCode: body.ResultCode, resultDescription: body.ResultDesc || "Payment failed" });
    }
    return res.json({ ResultCode: 0, ResultDesc: "Accepted" });
  } catch (error) {
    console.error(error);
    return res.json({ ResultCode: 0, ResultDesc: "Accepted" });
  }
});
