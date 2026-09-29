package com.skillx.server.infrastructure.payments

import com.skillx.server.configuration.StripeConfig
import com.stripe.Stripe
import com.stripe.exception.StripeException
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.PaymentIntentConfirmParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Stripe payment client for server-side payment processing.
 * Wraps the Stripe Java SDK with coroutine-friendly suspend functions.
 */
class StripeClient(private val config: StripeConfig) {

    init {
        Stripe.apiKey = config.secretKey
    }

    /**
     * Creates a PaymentIntent for the specified amount.
     * @param amountCents Amount in smallest currency unit (e.g., cents for USD)
     * @param currency Three-letter ISO currency code (default: "usd")
     * @param metadata Optional metadata to attach to the payment
     * @return The PaymentIntent client secret for client-side confirmation
     * @throws StripeException if the API call fails
     */
    suspend fun createPaymentIntent(
        amountCents: Long,
        currency: String = "usd",
        metadata: Map<String, String> = emptyMap()
    ): String = withContext(Dispatchers.IO) {
        val params = PaymentIntentCreateParams.builder()
            .setAmount(amountCents)
            .setCurrency(currency)
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .putAllMetadata(metadata)
            .build()

        val intent = PaymentIntent.create(params)
        intent.clientSecret ?: throw RuntimeException("Failed to create PaymentIntent: no client secret")
    }

    /**
     * Confirms a PaymentIntent on the server side.
     * Used when the client provides a payment method ID directly.
     * @param paymentIntentId The PaymentIntent ID to confirm
     * @param paymentMethodId The payment method ID to use
     * @return true if confirmation succeeded, false otherwise
     */
    suspend fun confirmPaymentIntent(
        paymentIntentId: String,
        paymentMethodId: String
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val params = PaymentIntentConfirmParams.builder()
                .setPaymentMethod(paymentMethodId)
                .build()

            val intent = PaymentIntent.retrieve(paymentIntentId).confirm(params)
            intent.status == "succeeded"
        } catch (e: StripeException) {
            false
        }
    }

    /**
     * Retrieves a PaymentIntent by ID.
     * @param paymentIntentId The PaymentIntent ID
     * @return The PaymentIntent, or null if not found
     */
    suspend fun retrievePaymentIntent(paymentIntentId: String): PaymentIntent? = withContext(Dispatchers.IO) {
        return@withContext try {
            PaymentIntent.retrieve(paymentIntentId)
        } catch (e: StripeException) {
            null
        }
    }

    /**
     * Refunds a completed PaymentIntent.
     * @param paymentIntentId The PaymentIntent ID to refund
     * @param amountCents Optional partial refund amount (null for full refund)
     * @return true if refund succeeded, false otherwise
     */
    suspend fun refundPaymentIntent(
        paymentIntentId: String,
        amountCents: Long? = null
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val refundParams = com.stripe.param.RefundCreateParams.builder()
                .setPaymentIntent(paymentIntentId)
                .apply { amountCents?.let { setAmount(it) } }
                .build()

            com.stripe.model.Refund.create(refundParams)
            true
        } catch (e: StripeException) {
            false
        }
    }
}