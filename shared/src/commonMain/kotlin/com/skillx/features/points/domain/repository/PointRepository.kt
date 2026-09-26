package com.skillx.features.points.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.points.domain.model.PointBalance
import com.skillx.features.points.domain.model.PointTransaction

/**
 * Point repository interface.
 * Balance reads and transaction history from server.
 */
interface PointRepository {
    suspend fun getBalance(): AppResult<PointBalance, AppError>
    suspend fun getTransactionHistory(): AppResult<List<PointTransaction>, AppError>
}
