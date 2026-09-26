package com.skillx.features.authentication.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.error.ValidationError
import com.skillx.core.result.AppResult
import com.skillx.core.validation.EmailValidator
import com.skillx.core.validation.PasswordValidator
import com.skillx.features.authentication.domain.model.AuthCredentials
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.repository.AuthRepository

/**
 * Registers a new user account.
 * Performs client-side validation, then delegates to the server.
 * The server grants initial 5 points — the client never decides its own balance.
 */
class RegisterUserUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): AppResult<AuthSession, AppError> {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return AppResult.Error(ValidationError.EmptyField)
        }

        if (!EmailValidator.isValid(email)) {
            return AppResult.Error(ValidationError.InvalidEmail())
        }

        if (!PasswordValidator.isValid(password)) {
            return AppResult.Error(ValidationError.WeakPassword())
        }

        return authRepository.register(
            AuthCredentials(
                email = email.trim(),
                password = password,
                name = name.trim()
            )
        )
    }
}
