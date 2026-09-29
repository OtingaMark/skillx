package com.skillx.features.users.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.users.data.mapper.UserProfileMapper
import com.skillx.features.users.data.remote.UserApi
import com.skillx.features.users.data.dto.UpdateProfileRequestDto
import com.skillx.features.users.data.dto.UserProfileDto
import com.skillx.features.users.domain.model.UserProfile
import com.skillx.features.users.domain.repository.UserRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*

class UserRepositoryImpl(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun getCurrentUser(): AppResult<UserProfile, AppError> {
        return try {
            val response = userApi.getCurrentUser()
            if (response.status.isSuccess()) {
                AppResult.Success(UserProfileMapper.toDomain(response.body<UserProfileDto>()))
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load profile."))
        }
    }

    override suspend fun getUserById(userId: String): AppResult<UserProfile, AppError> {
        return try {
            val response = userApi.getUserById(userId)
            if (response.status.isSuccess()) {
                AppResult.Success(UserProfileMapper.toDomain(response.body<UserProfileDto>()))
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load user."))
        }
    }

    override suspend fun updateProfile(
        name: String,
        teachSkills: List<String>,
        learnSkills: List<String>
    ): AppResult<UserProfile, AppError> {
        return try {
            val response = userApi.updateProfile(
                UpdateProfileRequestDto(name, teachSkills, learnSkills)
            )
            if (response.status.isSuccess()) {
                AppResult.Success(UserProfileMapper.toDomain(response.body<UserProfileDto>()))
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to update profile."))
        }
    }
}
