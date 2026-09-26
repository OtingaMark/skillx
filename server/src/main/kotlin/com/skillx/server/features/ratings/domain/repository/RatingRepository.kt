package com.skillx.server.features.ratings.domain.repository
interface RatingRepository { suspend fun submit(data: Map<String, Any>); suspend fun getSummary(userId: String): Pair<Double?, Int>; suspend fun hasRated(lessonId: String, raterId: String): Boolean }
