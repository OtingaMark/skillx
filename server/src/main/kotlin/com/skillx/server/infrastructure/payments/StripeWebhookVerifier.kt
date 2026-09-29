package com.skillx.server.infrastructure.payments

import com.skillx.server.configuration.StripeConfig
import com.stripe.exception.SignatureVerificationException
import com.stripe.net.Webhook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Stripe webhook signature verifier.
 * Validates incoming webhook payloads using the Stripe signing secret.
 */
class StripeWebhookVerifier(private val config: StripeConfig) {

    /**
     * Verifies a Stripe webhook signature.
     * @param payload The raw request body as a string
     * @param signature The value of the Stripe-Signature header
     * @return The parsed and verified event, or null if verification fails
     */
    suspend fun verifySignature(payload: String, signature: String): com.stripe.model.Event? = withContext(Dispatchers.IO) {
        return@withContext try {
            Webhook.constructEvent(payload, signature, config.webhookSecret)
        } catch (e: SignatureVerificationException) {
            // Invalid signature - log and return null
            null
        } catch (e: Exception) {
            // Other parsing errors
            null
        }
    }

    /**
     * Verifies a Stripe webhook signature and returns the event type.
     * Convenience method for routing webhooks by type.
     * @param payload The raw request body as a string
     * @param signature The value of the Stripe-Signature header
     * @return The event type string (e.g., "payment_intent.succeeded"), or null if verification fails
     */
    suspend fun verifyAndGetEventType(payload: String, signature: String): String? = withContext(Dispatchers.IO) {
        verifySignature(payload, signature)?.type
    }

    /**
     * Checks if the webhook secret is configured.
     * @return true if webhook secret is set, false otherwise
     */
    fun isConfigured(): Boolean = config.webhookSecret.isNotBlank()
}