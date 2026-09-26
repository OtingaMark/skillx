package com.skillx.server.configuration
data class StripeConfig(val secretKey: String, val webhookSecret: String, val publishableKey: String) {
    companion object { fun fromEnvironment() = StripeConfig(System.getenv("STRIPE_SECRET_KEY") ?: "", System.getenv("STRIPE_WEBHOOK_SECRET") ?: "", System.getenv("STRIPE_PUBLISHABLE_KEY") ?: "") }
}
