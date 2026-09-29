package com.skillx.server.features.authentication.domain.repository

/**
 * Persists password-reset tokens by their hash only — the raw token never touches storage.
 */
interface PasswordResetTokenRepository {

    /** Stores a new token for [userId] and invalidates any earlier unused ones for that user. */
    suspend fun issue(userId: String, tokenHash: String, expiresAtEpochMillis: Long, nowEpochMillis: Long)

    /**
     * Atomically: verifies the token exists, is unused and unexpired, marks it used, and
     * replaces the user's password hash. Also invalidates the user's other outstanding tokens.
     * Returns false (changing nothing) if the token is unknown, used, or expired.
     */
    suspend fun consume(tokenHash: String, newPasswordHash: String, nowEpochMillis: Long): Boolean
}
