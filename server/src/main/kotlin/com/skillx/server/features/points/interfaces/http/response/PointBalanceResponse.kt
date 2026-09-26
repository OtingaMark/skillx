package com.skillx.server.features.points.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class PointBalanceResponse(val userId: String, val points: Int)
