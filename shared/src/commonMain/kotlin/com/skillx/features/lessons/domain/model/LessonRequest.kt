package com.skillx.features.lessons.domain.model

/**
 * Domain model representing a lesson request between two students.
 * Extracted from data class LessonRequest in MainActivity.kt (L3299-3307).
 * Pure Kotlin — no Firebase or Compose imports.
 */
data class LessonRequest(
    val id: String,
    val requesterId: String,
    val teacherId: String,
    val requesterName: String,
    val teacherName: String,
    val skill: String,
    val status: LessonStatus
)
