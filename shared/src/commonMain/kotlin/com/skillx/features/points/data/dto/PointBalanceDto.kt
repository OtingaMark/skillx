package com.skillx.features.points.data.dto
import kotlinx.serialization.Serializable
@Serializable data class PointBalanceDto(val userId: String, val points: Int)
