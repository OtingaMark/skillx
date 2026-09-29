package com.skillx.features.onboarding.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.OnboardingProgress

interface OnboardingRepository {
    suspend fun loadProgress(): AppResult<OnboardingProgress, AppError>
    suspend fun saveTeachSkills(entries: List<com.skillx.features.onboarding.domain.model.UserSkillEntry>): AppResult<OnboardingProgress, AppError>
    suspend fun saveLearnSkills(entries: List<com.skillx.features.onboarding.domain.model.UserSkillEntry>): AppResult<OnboardingProgress, AppError>
    suspend fun saveProficiency(entries: List<com.skillx.features.onboarding.domain.model.UserSkillEntry>): AppResult<OnboardingProgress, AppError>
    suspend fun saveGoals(
        learning: Set<com.skillx.features.onboarding.domain.model.LearningGoal>,
        teaching: Set<com.skillx.features.onboarding.domain.model.TeachingGoal>
    ): AppResult<OnboardingProgress, AppError>
    suspend fun saveAvailability(
        days: Set<com.skillx.features.onboarding.domain.model.AvailabilityDay>,
        timesOfDay: Set<com.skillx.features.onboarding.domain.model.TimeOfDay>,
        formats: Set<com.skillx.features.onboarding.domain.model.LessonFormat>,
        durationMinutes: Int?,
        languages: List<com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry>
    ): AppResult<OnboardingProgress, AppError>
    suspend fun complete(): AppResult<OnboardingProgress, AppError>
}