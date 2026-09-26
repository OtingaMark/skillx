package com.skillx.server.features.lessons.domain.model
data class LessonRequest(val id: String, val requesterId: String, val teacherId: String, val requesterName: String, val teacherName: String, val skill: String, val status: LessonStatus)
