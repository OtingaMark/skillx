package com.skillx.server.features.ratings.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class RatingSummaryResponse(val userId: String, val averageRating: Double?, val totalRatings: Int)
