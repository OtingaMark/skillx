package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.features.authentication.configuration.PasswordResetConfig
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.features.authentication.domain.repository.PasswordResetTokenRepository
import com.skillx.server.features.authentication.infrastructure.security.ResetTokenGenerator
import com.skillx.server.infrastructure.email.EmailMessage
import com.skillx.server.infrastructure.email.EmailSender
import java.net.URLEncoder

/**
 * Issues a single-use reset token for the account behind [normalizedEmail] and emails the
 * reset link. Does nothing if no such account exists — callers must not reveal which happened.
 */
class IssuePasswordResetUseCase(
    private val authRepository: AuthRepository,
    private val tokenRepository: PasswordResetTokenRepository,
    private val tokenGenerator: ResetTokenGenerator,
    private val emailSender: EmailSender,
    private val config: PasswordResetConfig,
    private val now: () -> Long = System::currentTimeMillis
) {
    suspend operator fun invoke(normalizedEmail: String) {
        val user = authRepository.findByEmail(normalizedEmail) ?: return

        val token = tokenGenerator.generate()
        val issuedAt = now()
        tokenRepository.issue(
            userId = user.id,
            tokenHash = tokenGenerator.hash(token),
            expiresAtEpochMillis = issuedAt + config.tokenTtlMinutes * 60_000,
            nowEpochMillis = issuedAt
        )

        val link = "${config.linkBase}?token=${URLEncoder.encode(token, Charsets.UTF_8)}"
        emailSender.send(
            EmailMessage(
                to = user.email,
                subject = "Reset your SkillX password",
                textBody = """
                    |We received a request to reset the password for your SkillX account.
                    |
                    |Open this link on your phone to choose a new password:
                    |$link
                    |
                    |The link expires in ${config.tokenTtlMinutes} minutes and can be used once.
                    |If you didn't ask for this, you can ignore this email — your password won't change.
                """.trimMargin()
            )
        )
    }
}
