package com.skillx.features.onboarding.domain.usecase

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import com.skillx.features.onboarding.domain.repository.OnboardingRepository

class SaveTeachSkillsUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(entries: List<UserSkillEntry>): AppResult<OnboardingProgress, com.skillx.core.error.AppError> {
        return repository.saveTeachSkills(entries)
    }
}