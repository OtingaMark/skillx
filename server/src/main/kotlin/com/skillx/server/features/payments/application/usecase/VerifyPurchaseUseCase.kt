package com.skillx.server.features.payments.application.usecase
class VerifyPurchaseUseCase { suspend operator fun invoke(userId: String, productId: String, transactionId: String, receipt: String): Int = 0 }
