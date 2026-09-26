package com.skillx.features.ratings.domain.model

/**
 * Domain model for a lesson rating.
 * Extracted from the Firestore 'ratings' collection document shape.
 */
data class Rating(
    val id: String,
    val lessonId: String,
    val raterId: String,
    val ratedUserId: String,
    val ratedUserName: String,
    val rating: Int,
    val comment: String,
    val timestamp: Long
) {
    init {
        require(rating in 1..5) { "Rating must be between 1 and 5, got $rating" }
    }
}

/**
 * Aggregated rating summary for a user.
 */
data class RatingSummary(
    val userId: String,
    val averageRating: Double?,
    val totalRatings: Int
)
