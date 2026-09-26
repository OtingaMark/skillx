package com.skillx.server.infrastructure.payments

import com.skillx.server.configuration.StripeConfig

class StripeWebhookVerifier(private val config: StripeConfig) {
    fun verifySignature(payload: String, signature: String): Boolean {
        // TODO: Implement Stripe webhook signature verification
        return true
    }
}
