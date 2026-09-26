package com.skillx.server.features.lessons.interfaces.http.request
import kotlinx.serialization.Serializable
@Serializable data class CreateLessonRequestRequest(val teacherId: String, val skill: String)
