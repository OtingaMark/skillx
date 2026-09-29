package com.skillx.server.features.authentication.configuration

/** Password-reset settings, read from environment variables. */
data class PasswordResetConfig(
    /** Base of the link emailed to the user; the token is appended as `?token=`. */
    val linkBase: String,
    val tokenTtlMinutes: Long
) {
    companion object {
        fun fromEnvironment(): PasswordResetConfig = PasswordResetConfig(
            linkBase = System.getenv("PASSWORD_RESET_LINK_BASE")?.takeIf { it.isNotBlank() }
                ?: "skillx://reset-password",
            tokenTtlMinutes = System.getenv("PASSWORD_RESET_TOKEN_TTL_MINUTES")?.toLongOrNull() ?: 30
        )
    }
}
