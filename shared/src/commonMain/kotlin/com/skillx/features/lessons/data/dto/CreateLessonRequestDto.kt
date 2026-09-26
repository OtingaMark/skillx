package com.skillx.features.lessons.data.dto
import kotlinx.serialization.Serializable
@Serializable data class CreateLessonRequestDto(val teacherId: String, val skill: String)
