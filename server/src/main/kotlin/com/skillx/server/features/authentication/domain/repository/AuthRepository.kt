package com.skillx.server.features.authentication.domain.repository
interface AuthRepository { suspend fun findByEmail(email: String): Map<String, Any>?; suspend fun createUser(id: String, data: Map<String, Any>); suspend fun getPasswordHash(userId: String): String? }
