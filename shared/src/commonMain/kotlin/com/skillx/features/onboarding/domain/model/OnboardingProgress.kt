package com.skillx.features.onboarding.domain.model

import com.skillx.core.identifiers.UserId

/**
 * Domain model representing the user's onboarding progress.
 * Stored as a draft in Firestore during the wizard, then marked complete.
 */
data class OnboardingProgress(
    val userId: UserId,
    val teachSkills: List<UserSkillEntry> = emptyList(),
    val learnSkills: List<UserSkillEntry> = emptyList(),
    val learningGoals: Set<LearningGoal> = emptySet(),
    val teachingGoals: Set<TeachingGoal> = emptySet(),
    val availableDays: Set<AvailabilityDay> = emptySet(),
    val availableTimesOfDay: Set<TimeOfDay> = emptySet(),
    val lessonFormats: Set<LessonFormat> = emptySet(),
    val preferredDurationMinutes: Int? = null,
    val languages: List<LanguageProficiencyEntry> = emptyList(),
    val completedAtEpochMillis: Long? = null
)