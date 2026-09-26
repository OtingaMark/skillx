package com.skillx.features.ratings.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.ratings.domain.model.RatingSummary
import com.skillx.features.ratings.domain.repository.RatingRepository

class LoadRatingSummaryUseCase(private val repository: RatingRepository) {
    suspend operator fun invoke(userId: String): AppResult<RatingSummary, AppError> = repository.getRatingSummary(userId)
}
