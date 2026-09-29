package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.server.features.onboarding.domain.model.SkillRelation
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.onboarding.domain.model.UserSkillEntry
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.server.features.skills.application.usecase.AddLearningSkillUseCase
import com.skillx.server.features.skills.application.usecase.AddTeachingSkillUseCase
import com.skillx.server.features.users.application.usecase.UpdateUserProfileUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CompleteOnboardingUseCaseTest {

    private val userId = "user-123"
    private val skillId1 = "skill-1"
    private val skillId2 = "skill-2"

    private val onboardingRepository = mockk<OnboardingRepository>()
    private val addTeachingSkill = mockk<AddTeachingSkillUseCase>()
    private val addLearningSkill = mockk<AddLearningSkillUseCase>()
    private val updateUserProfile = mockk<UpdateUserProfileUseCase>()

    private val useCase = CompleteOnboardingUseCase(
        onboardingRepository = onboardingRepository,
        addTeachingSkill = addTeachingSkill,
        addLearningSkill = addLearningSkill,
        updateUserProfile = updateUserProfile
    )

    private val validProgress = OnboardingProgress(
        userId = userId,
        teachSkills = listOf(
            UserSkillEntry(skillId = skillId1, relation = SkillRelation.TEACH, proficiency = ProficiencyLevel.INTERMEDIATE)
        ),
        learnSkills = listOf(
            UserSkillEntry(skillId = skillId2, relation = SkillRelation.LEARN, targetProficiency = ProficiencyLevel.BEGINNER)
        ),
        learningGoals = setOf(LearningGoal.CAREER),
        teachingGoals = setOf(TeachingGoal.EARN_POINTS),
        availableDays = setOf(AvailabilityDay.MONDAY, AvailabilityDay.WEDNESDAY),
        availableTimesOfDay = setOf(TimeOfDay.EVENING),
        lessonFormats = setOf(LessonFormat.VIDEO),
        preferredDurationMinutes = 30,
        languages = listOf(LanguageProficiencyEntry("en", CefrLevel.C2))
    )

    @Test
    fun `complete onboarding with valid progress succeeds`() = runBlocking {
        coEvery { onboardingRepository.load(userId) } returns validProgress
        coEvery { onboardingRepository.markCompleted(userId) } returns validProgress.copy(completedAtEpochMillis = System.currentTimeMillis())
        coEvery { addTeachingSkill(any(), any()) } returns Unit
        coEvery { addLearningSkill(any(), any()) } returns Unit
        coEvery { updateUserProfile.applyOnboardingResult(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Unit

        val result = useCase(userId)

        assertEquals(userId, result.userId)
        coVerify(exactly = 1) { addTeachingSkill(userId, skillId1) }
        coVerify(exactly = 1) { addLearningSkill(userId, skillId2) }
        coVerify(exactly = 1) { updateUserProfile.applyOnboardingResult(
            userId = userId,
            onboardingCompleted = true,
            learningGoals = validProgress.learningGoals,
            teachingGoals = validProgress.teachingGoals,
            availableDays = validProgress.availableDays,
            availableTimesOfDay = validProgress.availableTimesOfDay,
            lessonFormats = validProgress.lessonFormats,
            preferredDurationMinutes = validProgress.preferredDurationMinutes,
            languages = validProgress.languages
        ) }
    }

    @Test
    fun `complete onboarding with no teach or learn skills throws validation error`() = runBlocking {
        val invalidProgress = validProgress.copy(teachSkills = emptyList(), learnSkills = emptyList())
        coEvery { onboardingRepository.load(userId) } returns invalidProgress

        val exception = assertFailsWith<ValidationException> {
            useCase(userId)
        }
        assertEquals("At least one teach skill or one learn skill is required to complete onboarding.", exception.message)
    }

    @Test
    fun `complete onboarding with teach skill missing proficiency throws validation error`() = runBlocking {
        val invalidProgress = validProgress.copy(
            teachSkills = listOf(UserSkillEntry(skillId = skillId1, relation = SkillRelation.TEACH, proficiency = null))
        )
        coEvery { onboardingRepository.load(userId) } returns invalidProgress

        val exception = assertFailsWith<ValidationException> {
            useCase(userId)
        }
        assertTrue(exception.message!!.contains("Proficiency is required"))
    }

    @Test
    fun `complete onboarding with no languages throws validation error`() = runBlocking {
        val invalidProgress = validProgress.copy(languages = emptyList())
        coEvery { onboardingRepository.load(userId) } returns invalidProgress

        val exception = assertFailsWith<ValidationException> {
            useCase(userId)
        }
        assertEquals("At least one language is required to complete onboarding.", exception.message)
    }

    @Test
    fun `complete onboarding with no available days throws validation error`() = runBlocking {
        val invalidProgress = validProgress.copy(availableDays = emptySet())
        coEvery { onboardingRepository.load(userId) } returns invalidProgress

        val exception = assertFailsWith<ValidationException> {
            useCase(userId)
        }
        assertEquals("At least one available day is required to complete onboarding.", exception.message)
    }
}
