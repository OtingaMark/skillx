package com.skillx.server.features.payments.domain.repository
interface PaymentRepository { suspend fun verifyPurchase(productId: String, transactionId: String): Boolean; suspend fun creditPoints(userId: String, points: Int) }
