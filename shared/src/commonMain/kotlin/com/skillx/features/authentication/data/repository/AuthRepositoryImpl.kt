package com.skillx.features.authentication.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.data.mapper.AuthMapper
import com.skillx.features.authentication.data.remote.AuthApi
import com.skillx.features.authentication.data.dto.AuthResponseDto
import com.skillx.features.authentication.data.dto.GoogleSignInRequestDto
import com.skillx.features.authentication.data.dto.LinkedInSignInRequestDto
import com.skillx.features.authentication.data.dto.LoginRequestDto
import com.skillx.features.authentication.data.dto.RegisterRequestDto
import com.skillx.features.authentication.domain.model.AuthCredentials
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.model.GoogleSignInCredential
import com.skillx.features.authentication.domain.repository.AuthRepository
import com.skillx.network.authentication.AuthTokenProvider
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Client-side implementation of AuthRepository.
 * Calls Ktor HTTP endpoints → server handles Firebase Auth + Firestore + JWT.
 */
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenProvider: AuthTokenProvider
) : AuthRepository {

    private val _sessionFlow = MutableStateFlow<AuthSession?>(null)

    override suspend fun register(credentials: AuthCredentials): AppResult<AuthSession, AppError> =
        handleAuthCall("Registration failed.") {
            authApi.register(
                RegisterRequestDto(
                    name = credentials.name,
                    email = credentials.email,
                    password = credentials.password
                )
            )
        }

    override suspend fun login(credentials: AuthCredentials): AppResult<AuthSession, AppError> =
        handleAuthCall("Login failed.") {
            authApi.login(
                LoginRequestDto(
                    email = credentials.email,
                    password = credentials.password
                )
            )
        }

    override suspend fun loginWithGoogle(credential: GoogleSignInCredential): AppResult<AuthSession, AppError> =
        handleAuthCall("Google sign-in failed.") {
            authApi.loginWithGoogle(
                when (credential) {
                    is GoogleSignInCredential.IdToken -> GoogleSignInRequestDto(idToken = credential.idToken)
                    is GoogleSignInCredential.AuthorizationCode -> GoogleSignInRequestDto(
                        code = credential.code,
                        codeVerifier = credential.codeVerifier
                    )
                }
            )
        }

    override suspend fun loginWithLinkedIn(code: String): AppResult<AuthSession, AppError> =
        handleAuthCall("LinkedIn sign-in failed.") {
            authApi.loginWithLinkedIn(LinkedInSignInRequestDto(code))
        }

    private suspend fun handleAuthCall(
        failureMessage: String,
        call: suspend () -> HttpResponse
    ): AppResult<AuthSession, AppError> {
        return try {
            val response = call()

            if (response.status.isSuccess()) {
                val dto = response.body<AuthResponseDto>()
                val session = AuthMapper.toDomain(dto)
                tokenProvider.saveTokens(session.token, session.refreshToken)
                _sessionFlow.value = session
                AppResult.Success(session)
            } else {
                val errorBody = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: failureMessage))
        }
    }

    override suspend fun logout(): AppResult<Unit, AppError> {
        return try {
            authApi.logout()
            tokenProvider.clearTokens()
            _sessionFlow.value = null
            AppResult.Success(Unit)
        } catch (e: Exception) {
            // Still clear local tokens even if server call fails
            tokenProvider.clearTokens()
            _sessionFlow.value = null
            AppResult.Success(Unit)
        }
    }

    override fun observeAuthSession(): Flow<AuthSession?> = _sessionFlow.asStateFlow()

    override suspend fun getStoredSession(): AuthSession? = _sessionFlow.value
}
