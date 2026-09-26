package com.skillx.server.features.lessons.domain.model
enum class LessonStatus { PENDING, ACCEPTED, COMPLETED; companion object { fun fromString(s: String) = entries.firstOrNull { it.name.equals(s, true) } ?: PENDING } }
