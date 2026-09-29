package com.skillx.features.authentication.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.repository.AuthRepository

/**
 * Authenticates via LinkedIn — same account whether the caller is signing up or logging in,
 * the server decides find-vs-create.
 */
class LoginWithLinkedInUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(code: String): AppResult<AuthSession, AppError> =
        authRepository.loginWithLinkedIn(code)
}
