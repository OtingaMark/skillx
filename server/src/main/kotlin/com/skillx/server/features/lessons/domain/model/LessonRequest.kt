package com.skillx.server.features.lessons.domain.model

/**
 * A lesson between a learner (requester) and a teacher.
 * The lesson plan, section progress and materials live on the same Firestore document.
 */
data class LessonRequest(
    val id: String,
    val requesterId: String,
    val teacherId: String,
    val requesterName: String,
    val teacherName: String,
    val skill: String,
    val status: LessonStatus,
    val sections: List<LessonSection> = emptyList(),
    val completedSectionIds: List<String> = emptyList(),
    val materials: List<LessonMaterial> = emptyList(),
    val scheduledAtEpochMillis: Long? = null,
    val completedAtEpochMillis: Long? = null,
    val pointsTransferred: Int? = null
)
