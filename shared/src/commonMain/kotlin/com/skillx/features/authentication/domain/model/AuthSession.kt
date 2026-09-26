package com.skillx.features.authentication.domain.model

/**
 * Represents an authenticated user session.
 * Contains the JWT token and basic user identity.
 */
data class AuthSession(
    val userId: String,
    val email: String,
    val token: String,
    val refreshToken: String
)
