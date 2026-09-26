package com.skillx.server.features.points.domain.model
data class PointTransaction(val id: String, val fromUserId: String, val toUserId: String, val amount: Int, val reason: String, val lessonId: String? = null, val timestamp: Long)
