package com.skillx.server.features.onboarding.domain.repository

import com.skillx.server.features.onboarding.domain.model.OnboardingProgress

interface OnboardingRepository {
    suspend fun load(userId: String): OnboardingProgress
    suspend fun saveTeachSkills(userId: String, entries: List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry>): OnboardingProgress
    suspend fun saveLearnSkills(userId: String, entries: List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry>): OnboardingProgress
    suspend fun saveProficiency(userId: String, entries: List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry>): OnboardingProgress
    suspend fun saveGoals(
        userId: String,
        learning: Set<com.skillx.server.features.onboarding.domain.model.LearningGoal>,
        teaching: Set<com.skillx.server.features.onboarding.domain.model.TeachingGoal>
    ): OnboardingProgress
    suspend fun saveAvailability(
        userId: String,
        days: Set<com.skillx.server.features.onboarding.domain.model.AvailabilityDay>,
        timesOfDay: Set<com.skillx.server.features.onboarding.domain.model.TimeOfDay>,
        formats: Set<com.skillx.server.features.onboarding.domain.model.LessonFormat>,
        durationMinutes: Int?,
        languages: List<com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry>
    ): OnboardingProgress
    suspend fun markCompleted(userId: String): OnboardingProgress
}