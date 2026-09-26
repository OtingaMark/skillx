package com.skillx.features.payments.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.payments.domain.model.PointPackage

/**
 * Payment repository interface.
 * Handles point package purchases — verification is server-side.
 */
interface PaymentRepository {
    suspend fun getPointPackages(): AppResult<List<PointPackage>, AppError>
    suspend fun verifyPurchase(
        productId: String,
        transactionId: String,
        receipt: String
    ): AppResult<Int, AppError> // Returns points credited
}
