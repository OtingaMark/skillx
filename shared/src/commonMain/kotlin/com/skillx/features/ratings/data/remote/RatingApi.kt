package com.skillx.features.ratings.data.remote

import com.skillx.features.ratings.data.remote.dto.SubmitRatingRequestDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class RatingApi(private val client: HttpClient) {
    suspend fun submitRating(request: SubmitRatingRequestDto): HttpResponse {
        return client.post("/api/v1/ratings") { setBody(request) }
    }
    suspend fun getRatingSummary(userId: String): HttpResponse {
        return client.get("/api/v1/ratings/summary/$userId")
    }
    suspend fun hasAlreadyRated(lessonId: String): HttpResponse {
        return client.get("/api/v1/ratings/check/$lessonId")
    }
}
