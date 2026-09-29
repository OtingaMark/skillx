package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository

class SaveAvailabilityUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(
        userId: String,
        days: Set<AvailabilityDay>,
        timesOfDay: Set<TimeOfDay>,
        formats: Set<LessonFormat>,
        durationMinutes: Int?,
        languages: List<LanguageProficiencyEntry>
    ): OnboardingProgress {
        return repository.saveAvailability(userId, days, timesOfDay, formats, durationMinutes, languages)
    }
}
