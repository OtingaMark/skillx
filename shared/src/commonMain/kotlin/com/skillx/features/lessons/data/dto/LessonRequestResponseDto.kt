package com.skillx.features.lessons.data.dto
import kotlinx.serialization.Serializable
@Serializable data class LessonRequestResponseDto(val id: String, val requesterId: String, val teacherId: String, val requesterName: String, val teacherName: String, val skill: String, val status: String)
