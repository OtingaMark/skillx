package com.skillx.server.features.lessons.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class LessonRequestResponse(val id: String, val requesterId: String, val teacherId: String, val requesterName: String, val teacherName: String, val skill: String, val status: String)
