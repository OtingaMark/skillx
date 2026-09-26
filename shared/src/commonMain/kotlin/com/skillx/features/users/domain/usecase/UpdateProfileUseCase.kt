package com.skillx.features.users.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.error.ValidationError
import com.skillx.core.result.AppResult
import com.skillx.features.users.domain.model.UserProfile
import com.skillx.features.users.domain.repository.UserRepository

/**
 * Updates the current user's profile.
 */
class UpdateProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        teachSkills: List<String>,
        learnSkills: List<String>
    ): AppResult<UserProfile, AppError> {
        if (name.isBlank()) {
            return AppResult.Error(ValidationError.Custom("Name cannot be empty."))
        }

        return userRepository.updateProfile(
            name = name.trim(),
            teachSkills = teachSkills.map { it.trim() }.filter { it.isNotEmpty() }.distinct(),
            learnSkills = learnSkills.map { it.trim() }.filter { it.isNotEmpty() }.distinct()
        )
    }
}
