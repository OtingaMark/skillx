package com.skillx.server.features.users.application.usecase

import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * Updates a user's profile.
 * Also handles applying onboarding completion results.
 */
class UpdateUserProfileUseCase(
    private val userRepository: UserRepository,
    private val transactionRunner: FirestoreTransactionRunner
) {

    /**
     * Updates basic profile fields (name, teach skills, learn skills).
     */
    suspend operator fun invoke(userId: String, name: String, teachSkills: List<String>, learnSkills: List<String>) {
        transactionRunner.runTransaction { tx ->
            userRepository.findById(userId, tx) ?: throw NotFoundException("User not found.")
            userRepository.update(
                userId,
                mapOf("name" to name, "teachSkills" to teachSkills, "learnSkills" to learnSkills),
                tx
            )
        }
    }

    /**
     * Applies the onboarding completion result to the user profile.
     * This is the only place where onboardingCompleted is set to true.
     */
    suspend fun applyOnboardingResult(
        userId: String,
        onboardingCompleted: Boolean,
        learningGoals: Set<LearningGoal>,
        teachingGoals: Set<TeachingGoal>,
        availableDays: Set<AvailabilityDay>,
        availableTimesOfDay: Set<TimeOfDay>,
        lessonFormats: Set<LessonFormat>,
        preferredDurationMinutes: Int?,
        languages: List<LanguageProficiencyEntry>
    ) {
        transactionRunner.runTransaction { tx ->
            userRepository.findById(userId, tx) ?: throw NotFoundException("User not found.")

            val updateData = mapOf(
                "onboardingCompleted" to onboardingCompleted,
                "learningGoals" to learningGoals.map { it.name },
                "teachingGoals" to teachingGoals.map { it.name },
                "availableDays" to availableDays.map { it.name },
                "availableTimesOfDay" to availableTimesOfDay.map { it.name },
                "lessonFormats" to lessonFormats.map { it.name },
                "preferredDurationMinutes" to preferredDurationMinutes,
                "languages" to languages.map { mapOf("languageCode" to it.languageCode, "level" to it.level.name) }
            )

            userRepository.update(userId, updateData, tx)
        }
    }
}
