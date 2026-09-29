package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.UserSkillEntry
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository

class SaveLearnSkillsUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(userId: String, entries: List<UserSkillEntry>): OnboardingProgress {
        return repository.saveLearnSkills(userId, entries)
    }
}