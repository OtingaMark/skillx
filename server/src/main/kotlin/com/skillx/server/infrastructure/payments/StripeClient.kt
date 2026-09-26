package com.skillx.server.infrastructure.payments
import com.skillx.server.configuration.StripeConfig

class StripeClient(private val config: StripeConfig) {
    fun createPaymentIntent(amountCents: Long, currency: String = "usd"): String {
        // Stripe API call
        return "pi_placeholder"
    }
    fun confirmPaymentIntent(paymentIntentId: String): Boolean {
        // Stripe API call
        return true
    }
}
