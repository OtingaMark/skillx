package com.skillx.server.features.onboarding.application.usecase

import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveAvailabilityUseCaseTest {

    private val userId = "user-1"
    private val repository = mockk<OnboardingRepository>()
    private val useCase = SaveAvailabilityUseCase(repository)

    @Test
    fun `times of day are persisted alongside days, formats, duration and languages`() = runBlocking {
        val days = setOf(AvailabilityDay.MONDAY, AvailabilityDay.SATURDAY)
        val times = setOf(TimeOfDay.EVENING, TimeOfDay.NIGHT)
        val formats = setOf(LessonFormat.VIDEO)
        val languages = listOf(LanguageProficiencyEntry("en", CefrLevel.C2))
        val saved = OnboardingProgress(
            userId = userId,
            availableDays = days,
            availableTimesOfDay = times,
            lessonFormats = formats,
            preferredDurationMinutes = 30,
            languages = languages
        )
        coEvery { repository.saveAvailability(userId, days, times, formats, 30, languages) } returns saved

        val result = useCase(userId, days, times, formats, 30, languages)

        assertEquals(times, result.availableTimesOfDay)
        coVerify(exactly = 1) { repository.saveAvailability(userId, days, times, formats, 30, languages) }
    }

    @Test
    fun `an empty time-of-day selection is saved as empty, not rejected`() = runBlocking {
        val days = setOf(AvailabilityDay.MONDAY)
        val saved = OnboardingProgress(userId = userId, availableDays = days)
        coEvery { repository.saveAvailability(userId, days, emptySet(), emptySet(), null, emptyList()) } returns saved

        val result = useCase(userId, days, emptySet(), emptySet(), null, emptyList())

        assertEquals(emptySet(), result.availableTimesOfDay)
    }
}
