package com.skillx.features.users.domain.model

import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.CefrLevel
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.model.TimeOfDay

/**
 * Domain model for a user's profile.
 * Extracted from the Firestore 'users' collection document shape.
 * Pure Kotlin — no Firebase, Compose, or Ktor imports.
 */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val points: Int,
    val onboardingCompleted: Boolean = false,
    val learningGoals: Set<LearningGoal> = emptySet(),
    val teachingGoals: Set<TeachingGoal> = emptySet(),
    val availableDays: Set<AvailabilityDay> = emptySet(),
    val availableTimesOfDay: Set<TimeOfDay> = emptySet(),
    val lessonFormats: Set<LessonFormat> = emptySet(),
    val preferredDurationMinutes: Int? = null,
    val languages: List<LanguageProficiencyEntry> = emptyList()
)
