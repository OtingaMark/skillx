package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.server.features.skills.application.usecase.AddLearningSkillUseCase
import com.skillx.server.features.skills.application.usecase.AddTeachingSkillUseCase
import com.skillx.server.features.users.application.usecase.UpdateUserProfileUseCase

/**
 * Completes the onboarding process.
 * This is the most architecturally important file in the onboarding feature:
 * it composes existing use cases rather than duplicating their logic.
 */
class CompleteOnboardingUseCase(
    private val onboardingRepository: OnboardingRepository,
    private val addTeachingSkill: AddTeachingSkillUseCase,
    private val addLearningSkill: AddLearningSkillUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase
) {
    suspend operator fun invoke(userId: String): OnboardingProgress {
        val progress = onboardingRepository.load(userId)

        // Validation: at least one teach or learn skill required
        if (progress.teachSkills.isEmpty() && progress.learnSkills.isEmpty()) {
            throw ValidationException(
                "AT_LEAST_ONE_SKILL_REQUIRED",
                "At least one teach skill or one learn skill is required to complete onboarding."
            )
        }

        // Validation: proficiency required for every teach skill
        progress.teachSkills.forEach { entry ->
            if (entry.proficiency == null) {
                throw ValidationException(
                    "PROFICIENCY_REQUIRED",
                    "Proficiency is required for every teach skill (skill: ${entry.skillId})."
                )
            }
        }

        // Validation: at least one language
        if (progress.languages.isEmpty()) {
            throw ValidationException("AT_LEAST_ONE_LANGUAGE_REQUIRED", "At least one language is required to complete onboarding.")
        }

        // Validation: at least one available day
        if (progress.availableDays.isEmpty()) {
            throw ValidationException("AT_LEAST_ONE_DAY_REQUIRED", "At least one available day is required to complete onboarding.")
        }

        // Delegate skill writes to existing skill feature use cases
        progress.teachSkills.forEach { entry ->
            addTeachingSkill(userId, entry.skillId)
        }
        progress.learnSkills.forEach { entry ->
            addLearningSkill(userId, entry.skillId)
        }

        // Update user profile with onboarding data
        updateUserProfile.applyOnboardingResult(
            userId = userId,
            onboardingCompleted = true,
            learningGoals = progress.learningGoals,
            teachingGoals = progress.teachingGoals,
            availableDays = progress.availableDays,
            availableTimesOfDay = progress.availableTimesOfDay,
            lessonFormats = progress.lessonFormats,
            preferredDurationMinutes = progress.preferredDurationMinutes,
            languages = progress.languages
        )

        // Mark onboarding as completed
        return onboardingRepository.markCompleted(userId)
    }
}