package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.core.exceptions.AuthenticationException
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.authentication.domain.model.AuthenticatedPrincipal
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.infrastructure.authentication.JwtTokenService
import com.skillx.server.infrastructure.authentication.PasswordHasher

/**
 * Authenticates an existing user and issues JWT tokens.
 * Verifies the password against the stored hash (supports Argon2id and bcrypt).
 * Rehashes the password with Argon2id if the stored hash uses an older algorithm.
 */
class LoginUserUseCase(
    private val authRepository: AuthRepository,
    private val jwtTokenService: JwtTokenService
) {

    /**
     * Authenticates a user with email and password.
     * @param email User's email (case-insensitive)
     * @param password Plain-text password
     * @return AuthenticatedPrincipal containing user ID, email, and JWT tokens
     * @throws ValidationException if input is invalid
     * @throws AuthenticationException if credentials are invalid
     */
    suspend operator fun invoke(email: String, password: String): AuthenticatedPrincipal {
        val normalizedEmail = email.trim().lowercase()

        if (normalizedEmail.isBlank() || password.isBlank()) {
            throw ValidationException("INVALID_INPUT", "Email and password are required.")
        }

        val user = authRepository.findByEmail(normalizedEmail)
            ?: throw AuthenticationException("Invalid email or password.")

        if (!PasswordHasher.verify(password, user.passwordHash)) {
            throw AuthenticationException("Invalid email or password.")
        }

        // Rehash with Argon2id if using older algorithm
        if (PasswordHasher.needsRehash(user.passwordHash)) {
            val newHash = PasswordHasher.rehash(password)
            authRepository.updatePasswordHash(user.id, newHash)
        }

        val token = jwtTokenService.generateToken(user.id, user.email)
        val refreshToken = jwtTokenService.generateRefreshToken(user.id)

        return AuthenticatedPrincipal(user.id, user.email, token, refreshToken)
    }
}