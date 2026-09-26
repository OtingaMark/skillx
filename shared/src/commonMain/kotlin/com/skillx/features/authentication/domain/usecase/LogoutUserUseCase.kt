package com.skillx.features.authentication.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.domain.repository.AuthRepository

/**
 * Logs the current user out and clears stored session.
 */
class LogoutUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AppResult<Unit, AppError> {
        return authRepository.logout()
    }
}
