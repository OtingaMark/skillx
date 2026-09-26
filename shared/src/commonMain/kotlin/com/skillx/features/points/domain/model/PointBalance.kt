package com.skillx.features.points.domain.model

/**
 * Domain model representing a user's current point balance.
 */
data class PointBalance(
    val userId: String,
    val points: Int
)
