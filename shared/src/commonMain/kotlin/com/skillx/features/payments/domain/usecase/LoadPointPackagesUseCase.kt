package com.skillx.features.payments.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.payments.domain.model.PointPackage
import com.skillx.features.payments.domain.repository.PaymentRepository

class LoadPointPackagesUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(): AppResult<List<PointPackage>, AppError> = repository.getPointPackages()
}
