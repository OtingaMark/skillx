package com.skillx.features.ratings.data.dto
import kotlinx.serialization.Serializable
@Serializable data class SubmitRatingRequestDto(val lessonId: String, val ratedUserId: String, val rating: Int, val comment: String)
