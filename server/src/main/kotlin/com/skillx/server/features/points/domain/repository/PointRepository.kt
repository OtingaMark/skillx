package com.skillx.server.features.points.domain.repository
interface PointRepository { suspend fun getBalance(userId: String): Int; suspend fun updateBalance(userId: String, newBalance: Int) }
