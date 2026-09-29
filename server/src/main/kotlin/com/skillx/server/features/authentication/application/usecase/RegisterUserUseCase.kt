package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.authentication.domain.model.AuthenticatedPrincipal
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.infrastructure.authentication.JwtTokenService
import com.skillx.server.infrastructure.authentication.PasswordHasher

/**
 * Registers a new user account and issues JWT tokens.
 * Hashes the password, creates the user document in Firestore, grants initial points, and returns tokens.
 */
class RegisterUserUseCase(
    private val authRepository: AuthRepository,
    private val jwtTokenService: JwtTokenService
) {

    /**
     * Registers a new user and returns authentication tokens.
     * @param name User's display name
     * @param email User's email (will be normalized to lowercase)
     * @param password Plain-text password (will be hashed with Argon2id)
     * @return AuthenticatedPrincipal containing user ID, email, and JWT tokens
     * @throws ValidationException if input is invalid
     * @throws ConflictException if email is already registered
     */
    suspend operator fun invoke(name: String, email: String, password: String): AuthenticatedPrincipal {
        val normalizedEmail = email.trim().lowercase()
        val trimmedName = name.trim()

        if (trimmedName.isBlank() || normalizedEmail.isBlank() || password.length < 6) {
            throw ValidationException("INVALID_INPUT", "Name, email, and password (min 6 chars) are required.")
        }

        // Check if email already exists
        if (authRepository.existsByEmail(normalizedEmail)) {
            throw ConflictException("EMAIL_EXISTS", "An account with this email already exists.")
        }

        val passwordHash = PasswordHasher.hash(password)
        val userId = authRepository.createUser(trimmedName, normalizedEmail, passwordHash)

        val token = jwtTokenService.generateToken(userId, normalizedEmail)
        val refreshToken = jwtTokenService.generateRefreshToken(userId)

        return AuthenticatedPrincipal(userId, normalizedEmail, token, refreshToken)
    }
}