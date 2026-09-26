package com.skillx.features.users.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.users.domain.model.UserProfile

/**
 * User repository interface.
 * Defines operations for loading and updating user profiles.
 */
interface UserRepository {
    suspend fun getCurrentUser(): AppResult<UserProfile, AppError>
    suspend fun getUserById(userId: String): AppResult<UserProfile, AppError>
    suspend fun updateProfile(
        name: String,
        teachSkills: List<String>,
        learnSkills: List<String>
    ): AppResult<UserProfile, AppError>
}
