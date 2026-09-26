package com.skillx.features.points.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.points.domain.model.PointTransaction
import com.skillx.features.points.domain.repository.PointRepository

class ObservePointHistoryUseCase(private val repository: PointRepository) {
    suspend operator fun invoke(): AppResult<List<PointTransaction>, AppError> = repository.getTransactionHistory()
}
