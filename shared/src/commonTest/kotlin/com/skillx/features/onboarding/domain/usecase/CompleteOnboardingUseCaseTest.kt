package com.skillx.features.onboarding.domain.usecase

import com.skillx.core.identifiers.UserId
import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.CefrLevel
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.features.onboarding.domain.model.SkillRelation
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CompleteOnboardingUseCaseTest {

    private val userId = UserId("user-123")
    private val skillId1 = com.skillx.core.identifiers.SkillId("skill-1")
    private val skillId2 = com.skillx.core.identifiers.SkillId("skill-2")

    private val onboardingRepository = mockk<OnboardingRepository>()

    private val useCase = CompleteOnboardingUseCase(onboardingRepository)

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
        lessonFormats = setOf(LessonFormat.VIDEO),
        preferredDurationMinutes = 30,
        languages = listOf(LanguageProficiencyEntry("en", CefrLevel.C2))
    )

    @Test
    fun `complete onboarding with valid progress succeeds`() = runBlocking {
        every { onboardingRepository.complete() } returns AppResult.Success(validProgress.copy(completedAtEpochMillis = System.currentTimeMillis()))

        val result = useCase()

        assertEquals(AppResult.Success::class, result::class)
        val progress = result.getOrNull()
        assertEquals(userId, progress?.userId)
        assertEquals(validProgress.completedAtEpochMillis != null, progress?.completedAtEpochMillis != null)
        verify(exactly = 1) { onboardingRepository.complete() }
    }

    @Test
    fun `complete onboarding with repository error returns error`() = runBlocking {
        every { onboardingRepository.complete() } returns AppResult.Error(com.skillx.core.error.NetworkError.Unknown("Network error"))

        val result = useCase()

        assertEquals(AppResult.Error::class, result::class)
        val error = result.errorOrNull()
        assertEquals("Network error", error?.message)
        verify(exactly = 1) { onboardingRepository.complete() }
    }
}