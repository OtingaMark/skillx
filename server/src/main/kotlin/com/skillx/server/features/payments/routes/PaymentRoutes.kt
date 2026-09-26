package com.skillx.server.features.payments.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class PackageResponse(val productId: String, val points: Int, val formattedPrice: String)
@Serializable data class VerifyPurchaseRequest(val productId: String, val transactionId: String, val receipt: String)
@Serializable data class VerifyPurchaseResponse(val pointsCredited: Int)

private val PRODUCT_POINTS = mapOf("skillx_points_5" to 5, "skillx_points_15" to 15, "skillx_points_30" to 30)

fun Route.paymentRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/payments") {
        get("/packages") {
            call.respond(listOf(PackageResponse("skillx_points_5", 5, "$0.99"), PackageResponse("skillx_points_15", 15, "$2.49"), PackageResponse("skillx_points_30", 30, "$3.99")))
        }
        post("/verify") {
            val uid = call.userId()
            val request = call.receive<VerifyPurchaseRequest>()
            val points = PRODUCT_POINTS[request.productId] ?: throw ValidationException("INVALID_PRODUCT", "Unknown product.")
            // TODO: Verify receipt with RevenueCat/Play Store API in production
            val db = firestoreProvider.getFirestore()
            val userRef = db.collection("users").document(uid)
            db.runTransaction { tx ->
                val doc = tx.get(userRef).get()
                val currentPoints = (doc.getLong("points") ?: 0).toInt()
                tx.update(userRef, "points", currentPoints + points)
                val txId = java.util.UUID.randomUUID().toString()
                val ledger = mapOf("fromUserId" to "SYSTEM", "toUserId" to uid, "amount" to points,
                    "reason" to "PURCHASE", "timestamp" to System.currentTimeMillis())
                tx.set(db.collection("pointTransactions").document(txId), ledger)
            }.get()
            call.respond(HttpStatusCode.OK, VerifyPurchaseResponse(points))
        }
    }
}
