package com.skillx.server.features.payments.application.usecase
class HandleStripeWebhookUseCase { suspend operator fun invoke(payload: String, signature: String) = Unit }
