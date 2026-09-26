package com.skillx.features.payments.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.payments.domain.repository.PaymentRepository

/**
 * Verifies a purchase with the server and credits points.
 * The client NEVER increments its own Firestore points field.
 * Server-side verification is the only authority (Section 6).
 */
class PurchasePointPackageUseCase(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        productId: String,
        transactionId: String,
        receipt: String
    ): AppResult<Int, AppError> {
        return paymentRepository.verifyPurchase(
            productId = productId,
            transactionId = transactionId,
            receipt = receipt
        )
    }
}
