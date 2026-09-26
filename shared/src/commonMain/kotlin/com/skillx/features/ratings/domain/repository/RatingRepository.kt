package com.skillx.features.ratings.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.ratings.domain.model.Rating
import com.skillx.features.ratings.domain.model.RatingSummary

/**
 * Rating repository interface.
 * Submit and query ratings through server API.
 */
interface RatingRepository {
    suspend fun submitRating(
        lessonId: String,
        ratedUserId: String,
        rating: Int,
        comment: String
    ): AppResult<Rating, AppError>

    suspend fun getRatingSummary(userId: String): AppResult<RatingSummary, AppError>

    suspend fun hasAlreadyRated(lessonId: String): AppResult<Boolean, AppError>
}
