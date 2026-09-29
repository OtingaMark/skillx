package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.authentication.domain.model.PasswordPolicy
import com.skillx.server.features.authentication.domain.repository.PasswordResetTokenRepository
import com.skillx.server.features.authentication.infrastructure.security.ResetTokenGenerator
import com.skillx.server.infrastructure.authentication.PasswordHasher

/** Sets a new password using a single-use, unexpired reset token. */
class ResetPasswordUseCase(
    private val tokenRepository: PasswordResetTokenRepository,
    private val tokenGenerator: ResetTokenGenerator,
    private val now: () -> Long = System::currentTimeMillis
) {
    suspend operator fun invoke(token: String, newPassword: String) {
        if (token.isBlank()) throw invalidToken()
        PasswordPolicy.requireValid(newPassword)

        val consumed = tokenRepository.consume(
            tokenHash = tokenGenerator.hash(token.trim()),
            newPasswordHash = PasswordHasher.hash(newPassword),
            nowEpochMillis = now()
        )
        if (!consumed) throw invalidToken()
    }

    private fun invalidToken() =
        ValidationException("INVALID_RESET_TOKEN", "This reset link is invalid or has expired. Please request a new one.")
}
