package com.skillx.server.features.points.domain.repository
interface PointLedgerRepository { suspend fun recordTransaction(data: Map<String, Any>); suspend fun getHistory(userId: String): List<Map<String, Any>> }
