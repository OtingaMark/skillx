package com.skillx.server.features.matching.domain.repository
interface MatchRepository { suspend fun findMatches(userId: String): List<Map<String, Any>> }
