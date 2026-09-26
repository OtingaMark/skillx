package com.skillx.server.features.points.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class PointTransactionResponse(val id: String, val fromUserId: String, val toUserId: String, val amount: Int, val reason: String, val lessonId: String? = null, val timestamp: Long)
