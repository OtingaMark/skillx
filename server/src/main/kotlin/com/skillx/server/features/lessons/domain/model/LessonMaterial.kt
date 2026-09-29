package com.skillx.server.features.lessons.domain.model

/** A resource the teacher attached to a lesson, referenced by its HTTPS URL. */
data class LessonMaterial(
    val id: String,
    val title: String,
    val type: MaterialType,
    val url: String,
    val addedAtEpochMillis: Long
)
