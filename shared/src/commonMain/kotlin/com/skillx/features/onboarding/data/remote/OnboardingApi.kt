package com.skillx.features.onboarding.data.remote

import com.skillx.features.onboarding.data.dto.LanguageProficiencyEntryDto
import com.skillx.features.onboarding.data.dto.OnboardingProgressDto
import com.skillx.features.onboarding.data.dto.SaveAvailabilityRequestDto
import com.skillx.features.onboarding.data.dto.SaveGoalsRequestDto
import com.skillx.features.onboarding.data.dto.SaveLearnSkillsRequestDto
import com.skillx.features.onboarding.data.dto.SaveProficiencyRequestDto
import com.skillx.features.onboarding.data.dto.SaveTeachSkillsRequestDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.flow.Flow

/**
 * Ktor client API for onboarding endpoints.
 */
class OnboardingApi(private val client: HttpClient) {

    suspend fun loadProgress(): HttpResponse {
        return client.get("/api/v1/onboarding/progress")
    }

    suspend fun saveTeachSkills(request: SaveTeachSkillsRequestDto): HttpResponse {
        return client.post("/api/v1/onboarding/skills/teach") {
            setBody(request)
        }
    }

    suspend fun saveLearnSkills(request: SaveLearnSkillsRequestDto): HttpResponse {
        return client.post("/api/v1/onboarding/skills/learn") {
            setBody(request)
        }
    }

    suspend fun saveProficiency(request: SaveProficiencyRequestDto): HttpResponse {
        return client.post("/api/v1/onboarding/proficiency") {
            setBody(request)
        }
    }

    suspend fun saveGoals(request: SaveGoalsRequestDto): HttpResponse {
        return client.post("/api/v1/onboarding/goals") {
            setBody(request)
        }
    }

    suspend fun saveAvailability(request: SaveAvailabilityRequestDto): HttpResponse {
        return client.post("/api/v1/onboarding/availability") {
            setBody(request)
        }
    }

    suspend fun completeOnboarding(): HttpResponse {
        return client.post("/api/v1/onboarding/complete")
    }
}