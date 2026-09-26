package com.skillx.server.features.ratings.domain.model
data class Rating(val id: String, val lessonId: String, val raterId: String, val ratedUserId: String, val rating: Int, val comment: String, val timestamp: Long)
