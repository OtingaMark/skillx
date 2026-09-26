package com.skillx.features.authentication.domain.usecase

import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

/**
 * Observes the current authentication session state as a Flow.
 * Emits null when the user is logged out.
 */
class ObserveAuthSessionUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthSession?> {
        return authRepository.observeAuthSession()
    }
}
