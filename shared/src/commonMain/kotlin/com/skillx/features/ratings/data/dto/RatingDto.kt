package com.skillx.features.ratings.data.dto
import kotlinx.serialization.Serializable
@Serializable data class RatingDto(
    val id: String,
    val lessonId: String,
    val raterId: String,
    val ratedUserId: String,
    val ratedUserName: String,
    val rating: Int,
    val comment: String,
    val timestamp: Long
)
