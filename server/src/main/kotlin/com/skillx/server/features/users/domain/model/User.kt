package com.skillx.server.features.users.domain.model

import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay

/**
 * Domain model for a user.
 * Includes passwordHash for authentication (server-only, never exposed via API).
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val points: Int,
    val passwordHash: String = "",
    val onboardingCompleted: Boolean = false,
    val learningGoals: Set<LearningGoal> = emptySet(),
    val teachingGoals: Set<TeachingGoal> = emptySet(),
    val availableDays: Set<AvailabilityDay> = emptySet(),
    val availableTimesOfDay: Set<TimeOfDay> = emptySet(),
    val lessonFormats: Set<LessonFormat> = emptySet(),
    val preferredDurationMinutes: Int? = null,
    val languages: List<LanguageProficiencyEntry> = emptyList()
)