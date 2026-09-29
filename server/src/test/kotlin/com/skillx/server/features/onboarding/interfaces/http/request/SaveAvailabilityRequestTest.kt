package com.skillx.server.features.onboarding.interfaces.http.request

import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SaveAvailabilityRequestTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `times of day parse into the domain enum`() {
        val request = json.decodeFromString<SaveAvailabilityRequest>(
            """{"days":["MONDAY"],"timesOfDay":["MORNING","EVENING"],"lessonFormats":[],"preferredDurationMinutes":null,"languages":[]}"""
        )

        assertEquals(setOf(TimeOfDay.MORNING, TimeOfDay.EVENING), request.timesOfDaySet())
    }

    @Test
    fun `a request without timesOfDay still deserializes and defaults to empty`() {
        val request = json.decodeFromString<SaveAvailabilityRequest>(
            """{"days":["MONDAY"],"lessonFormats":[],"preferredDurationMinutes":null,"languages":[]}"""
        )

        assertEquals(emptySet(), request.timesOfDaySet())
    }
}
