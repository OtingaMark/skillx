package com.skillx.server.features.lessons.domain.repository
interface LessonRepository { suspend fun create(data: Map<String, Any>): String; suspend fun findById(id: String): Map<String, Any>?; suspend fun findByUser(userId: String): List<Map<String, Any>>; suspend fun updateStatus(id: String, status: String) }
