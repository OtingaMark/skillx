package com.skillx.features.ratings.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.error.ValidationError
import com.skillx.core.result.AppResult
import com.skillx.features.ratings.domain.model.Rating
import com.skillx.features.ratings.domain.repository.RatingRepository

/**
 * Submits a rating for a completed lesson.
 * Duplicate-rating check is server-authoritative.
 */
class SubmitRatingUseCase(
    private val ratingRepository: RatingRepository
) {
    suspend operator fun invoke(
        lessonId: String,
        ratedUserId: String,
        rating: Int,
        comment: String
    ): AppResult<Rating, AppError> {
        if (rating !in 1..5) {
            return AppResult.Error(ValidationError.InvalidRating())
        }

        return ratingRepository.submitRating(
            lessonId = lessonId,
            ratedUserId = ratedUserId,
            rating = rating,
            comment = comment.trim()
        )
    }
}
