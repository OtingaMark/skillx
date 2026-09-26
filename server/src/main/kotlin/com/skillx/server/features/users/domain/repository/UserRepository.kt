package com.skillx.server.features.users.domain.repository
import com.skillx.server.features.users.domain.model.User
interface UserRepository { suspend fun findById(id: String): User?; suspend fun update(id: String, data: Map<String, Any>) }
