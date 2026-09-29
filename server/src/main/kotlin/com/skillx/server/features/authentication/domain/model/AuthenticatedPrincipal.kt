package com.skillx.server.features.authentication.domain.model

/**
 * Represents an authenticated user principal with JWT tokens.
 */
data class AuthenticatedPrincipal(
    val userId: String,
    val email: String,
    val token: String,
    val refreshToken: String
)