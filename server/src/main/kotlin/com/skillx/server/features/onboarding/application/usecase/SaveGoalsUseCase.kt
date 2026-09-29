package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository

class SaveGoalsUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(
        userId: String,
        learning: Set<LearningGoal>,
        teaching: Set<TeachingGoal>
    ): OnboardingProgress {
        return repository.saveGoals(userId, learning, teaching)
    }
}