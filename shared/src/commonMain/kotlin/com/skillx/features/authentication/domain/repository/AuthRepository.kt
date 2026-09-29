package com.skillx.features.authentication.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.domain.model.AuthCredentials
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.model.GoogleSignInCredential
import kotlinx.coroutines.flow.Flow

/**
 * Authentication repository interface.
 * Client implementation calls Ktor HTTP → server.
 * Server implementation manages Firebase Auth + Firestore + JWT.
 */
interface AuthRepository {
    suspend fun register(credentials: AuthCredentials): AppResult<AuthSession, AppError>
    suspend fun login(credentials: AuthCredentials): AppResult<AuthSession, AppError>
    suspend fun loginWithGoogle(credential: GoogleSignInCredential): AppResult<AuthSession, AppError>
    suspend fun loginWithLinkedIn(code: String): AppResult<AuthSession, AppError>
    suspend fun logout(): AppResult<Unit, AppError>
    fun observeAuthSession(): Flow<AuthSession?>
    suspend fun getStoredSession(): AuthSession?
}
