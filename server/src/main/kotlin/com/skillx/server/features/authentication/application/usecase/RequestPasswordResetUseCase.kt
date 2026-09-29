package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

/**
 * Entry point for "forgot password". Returns immediately and identically whether or not the
 * account exists: the lookup, token issue and email all run in [backgroundScope], so neither
 * the response nor its timing reveals account existence.
 */
class RequestPasswordResetUseCase(
    private val issuePasswordReset: IssuePasswordResetUseCase,
    private val backgroundScope: CoroutineScope
) {
    private val logger = LoggerFactory.getLogger(RequestPasswordResetUseCase::class.java)

    operator fun invoke(email: String) {
        val normalizedEmail = email.trim().lowercase()
        if (normalizedEmail.isBlank() || !normalizedEmail.contains('@')) {
            throw ValidationException("INVALID_EMAIL", "Please enter a valid email address.")
        }

        backgroundScope.launch {
            try {
                issuePasswordReset(normalizedEmail)
            } catch (e: Exception) {
                // Never log the email or token — only that delivery failed and why.
                logger.error("Password reset issuance failed: ${e::class.simpleName}: ${e.message}")
            }
        }
    }
}
