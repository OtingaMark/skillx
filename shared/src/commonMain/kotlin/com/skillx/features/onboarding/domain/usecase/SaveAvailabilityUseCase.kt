package com.skillx.features.onboarding.domain.usecase

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.TimeOfDay
import com.skillx.features.onboarding.domain.repository.OnboardingRepository

class SaveAvailabilityUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(
        days: Set<AvailabilityDay>,
        timesOfDay: Set<TimeOfDay>,
        formats: Set<LessonFormat>,
        durationMinutes: Int?,
        languages: List<LanguageProficiencyEntry>
    ): AppResult<OnboardingProgress, com.skillx.core.error.AppError> {
        return repository.saveAvailability(days, timesOfDay, formats, durationMinutes, languages)
    }
}