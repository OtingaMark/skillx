package com.skillx.features.ratings.data.dto
import kotlinx.serialization.Serializable
@Serializable data class RatingSummaryDto(val userId: String, val averageRating: Double?, val totalRatings: Int)
