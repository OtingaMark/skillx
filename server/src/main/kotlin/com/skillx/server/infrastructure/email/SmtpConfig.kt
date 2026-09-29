package com.skillx.server.infrastructure.email

/**
 * SMTP settings, read only from environment variables.
 * [host] is null when SMTP_HOST is unset — the sender then refuses to send rather than
 * pretending to succeed.
 */
data class SmtpConfig(
    val host: String?,
    val port: Int,
    val username: String?,
    val password: String?,
    val fromAddress: String?,
    val security: SmtpSecurity
) {
    enum class SmtpSecurity { NONE, STARTTLS, SSL }

    companion object {
        fun fromEnvironment(): SmtpConfig {
            val security = System.getenv("SMTP_SECURITY")?.uppercase()?.let { SmtpSecurity.valueOf(it) }
                ?: SmtpSecurity.STARTTLS
            val defaultPort = when (security) {
                SmtpSecurity.NONE -> 25
                SmtpSecurity.STARTTLS -> 587
                SmtpSecurity.SSL -> 465
            }
            return SmtpConfig(
                host = System.getenv("SMTP_HOST")?.takeIf { it.isNotBlank() },
                port = System.getenv("SMTP_PORT")?.toIntOrNull() ?: defaultPort,
                username = System.getenv("SMTP_USERNAME")?.takeIf { it.isNotBlank() },
                password = System.getenv("SMTP_PASSWORD")?.takeIf { it.isNotBlank() },
                fromAddress = System.getenv("SMTP_FROM")?.takeIf { it.isNotBlank() },
                security = security
            )
        }
    }
}
