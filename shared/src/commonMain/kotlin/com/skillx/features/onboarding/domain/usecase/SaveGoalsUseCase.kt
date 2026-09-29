package com.skillx.features.onboarding.domain.usecase

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.repository.OnboardingRepository

class SaveGoalsUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(
        learning: Set<LearningGoal>,
        teaching: Set<TeachingGoal>
    ): AppResult<OnboardingProgress, com.skillx.core.error.AppError> {
        return repository.saveGoals(learning, teaching)
    }
}