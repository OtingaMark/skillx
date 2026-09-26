package com.skillx.features.users.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.users.domain.model.UserProfile
import com.skillx.features.users.domain.repository.UserRepository

/**
 * Loads the currently authenticated user's profile.
 */
class LoadCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): AppResult<UserProfile, AppError> {
        return userRepository.getCurrentUser()
    }
}
