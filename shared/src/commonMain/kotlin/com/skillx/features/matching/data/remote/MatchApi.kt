package com.skillx.features.matching.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class MatchApi(private val client: HttpClient) {
    suspend fun findMatches(): HttpResponse {
        return client.get("/api/v1/matches")
    }
}
