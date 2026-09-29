package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository

class LoadOnboardingProgressUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(userId: String): OnboardingProgress {
        return repository.load(userId)
    }
}