package com.skillx.features.points.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.points.domain.model.PointBalance
import com.skillx.features.points.domain.repository.PointRepository

class LoadPointBalanceUseCase(private val repository: PointRepository) {
    suspend operator fun invoke(): AppResult<PointBalance, AppError> = repository.getBalance()
}
