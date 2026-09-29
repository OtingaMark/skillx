package com.skillx.server.features.onboarding.domain.model

data class OnboardingProgress(
    val userId: String,
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