package com.skillx.features.lessons.domain.model

/**
 * Enum representing the lifecycle states of a lesson request.
 * Replaces the raw string statuses ("pending", "accepted", "completed")
 * used in the original MainActivity.kt.
 */
enum class LessonStatus {
    PENDING,
    ACCEPTED,
    COMPLETED;

    fun asString(): String = name.lowercase()

    companion object {
        fun fromString(value: String): LessonStatus {
            return when (value.lowercase().trim()) {
                "pending" -> PENDING
                "accepted" -> ACCEPTED
                "completed" -> COMPLETED
                else -> PENDING
            }
        }
    }
}
