package com.skillx.server.features.ratings.interfaces.http.request
import kotlinx.serialization.Serializable
@Serializable data class SubmitRatingRequest(val lessonId: String, val ratedUserId: String, val rating: Int, val comment: String)
