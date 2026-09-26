package com.skillx.server.features.payments.interfaces.http
// WebhookRoutes for Stripe/RevenueCat callbacks
import io.ktor.server.routing.*
fun Route.webhookRoutes() { route("/webhooks") { post("/stripe") { /* Stripe webhook */ }; post("/revenuecat") { /* RevenueCat webhook */ } } }
