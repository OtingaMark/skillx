package com.skillx.features.authentication.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.error.ValidationError
import com.skillx.core.result.AppResult
import com.skillx.core.validation.EmailValidator
import com.skillx.features.authentication.domain.model.AuthCredentials
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.repository.AuthRepository

/**
 * Authenticates an existing user.
 */
class LoginUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): AppResult<AuthSession, AppError> {
        if (email.isBlank() || password.isBlank()) {
            return AppResult.Error(ValidationError.EmptyField)
        }

        if (!EmailValidator.isValid(email)) {
            return AppResult.Error(ValidationError.InvalidEmail())
        }

        return authRepository.login(
            AuthCredentials(
                email = email.trim(),
                password = password
            )
        )
    }
}
