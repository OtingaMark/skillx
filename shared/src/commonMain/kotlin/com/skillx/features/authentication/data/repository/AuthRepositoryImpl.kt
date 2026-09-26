package com.skillx.features.authentication.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.data.mapper.AuthMapper
import com.skillx.features.authentication.data.remote.AuthApi
import com.skillx.features.authentication.data.remote.dto.AuthResponseDto
import com.skillx.features.authentication.data.remote.dto.LoginRequestDto
import com.skillx.features.authentication.data.remote.dto.RegisterRequestDto
import com.skillx.features.authentication.domain.model.AuthCredentials
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.repository.AuthRepository
import com.skillx.network.authentication.AuthTokenProvider
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
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

    override suspend fun register(credentials: AuthCredentials): AppResult<AuthSession, AppError> {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    name = credentials.name,
                    email = credentials.email,
                    password = credentials.password
                )
            )

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
            AppResult.Error(NetworkError.Unknown(e.message ?: "Registration failed."))
        }
    }

    override suspend fun login(credentials: AuthCredentials): AppResult<AuthSession, AppError> {
        return try {
            val response = authApi.login(
                LoginRequestDto(
                    email = credentials.email,
                    password = credentials.password
                )
            )

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
            AppResult.Error(NetworkError.Unknown(e.message ?: "Login failed."))
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
