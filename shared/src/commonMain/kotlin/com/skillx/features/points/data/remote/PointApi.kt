package com.skillx.features.points.data.remote

import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

@Serializable
data class PointBalanceDto(val userId: String, val points: Int)

@Serializable
data class PointTransactionDto(
    val id: String, val fromUserId: String, val toUserId: String,
    val amount: Int, val reason: String, val lessonId: String? = null, val timestamp: Long
)

class PointApi(private val client: HttpClient) {
    suspend fun getBalance(): HttpResponse = client.get("/api/v1/points/balance")
    suspend fun getHistory(): HttpResponse = client.get("/api/v1/points/history")
}
