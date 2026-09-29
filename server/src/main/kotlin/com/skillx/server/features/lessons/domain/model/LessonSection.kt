package com.skillx.server.features.lessons.domain.model

/** One step of a lesson plan. Order is the section's position in the plan. */
data class LessonSection(
    val id: String,
    val title: String
)
