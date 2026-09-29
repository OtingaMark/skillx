package com.skillx.features.onboarding.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.data.dto.LanguageProficiencyEntryDto
import com.skillx.features.onboarding.data.dto.OnboardingProgressDto
import com.skillx.features.onboarding.data.dto.SaveAvailabilityRequestDto
import com.skillx.features.onboarding.data.dto.SaveGoalsRequestDto
import com.skillx.features.onboarding.data.dto.SaveLearnSkillsRequestDto
import com.skillx.features.onboarding.data.dto.SaveProficiencyRequestDto
import com.skillx.features.onboarding.data.dto.SaveTeachSkillsRequestDto
import com.skillx.features.onboarding.data.mapper.OnboardingMapper
import com.skillx.features.onboarding.data.remote.OnboardingApi
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.network.error.ApiErrorMapper
import io.ktor.client.call.*
import io.ktor.http.*

class OnboardingRepositoryImpl(
    private val onboardingApi: OnboardingApi
) : OnboardingRepository {

    override suspend fun loadProgress(): AppResult<OnboardingProgress, AppError> {
        return try {
            val response = onboardingApi.loadProgress()
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load onboarding progress."))
        }
    }

    override suspend fun saveTeachSkills(entries: List<com.skillx.features.onboarding.domain.model.UserSkillEntry>): AppResult<OnboardingProgress, AppError> {
        return try {
            val request = SaveTeachSkillsRequestDto(
                skills = entries.map { OnboardingMapper.toDto(it) }
            )
            val response = onboardingApi.saveTeachSkills(request)
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to save teach skills."))
        }
    }

    override suspend fun saveLearnSkills(entries: List<com.skillx.features.onboarding.domain.model.UserSkillEntry>): AppResult<OnboardingProgress, AppError> {
        return try {
            val request = SaveLearnSkillsRequestDto(
                skills = entries.map { OnboardingMapper.toDto(it) }
            )
            val response = onboardingApi.saveLearnSkills(request)
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to save learn skills."))
        }
    }

    override suspend fun saveProficiency(entries: List<com.skillx.features.onboarding.domain.model.UserSkillEntry>): AppResult<OnboardingProgress, AppError> {
        return try {
            val request = SaveProficiencyRequestDto(
                skills = entries.map { OnboardingMapper.toDto(it) }
            )
            val response = onboardingApi.saveProficiency(request)
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to save proficiency."))
        }
    }

    override suspend fun saveGoals(
        learning: Set<com.skillx.features.onboarding.domain.model.LearningGoal>,
        teaching: Set<com.skillx.features.onboarding.domain.model.TeachingGoal>
    ): AppResult<OnboardingProgress, AppError> {
        return try {
            val request = SaveGoalsRequestDto(
                learningGoals = learning.map { it.name },
                teachingGoals = teaching.map { it.name }
            )
            val response = onboardingApi.saveGoals(request)
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to save goals."))
        }
    }

    override suspend fun saveAvailability(
        days: Set<com.skillx.features.onboarding.domain.model.AvailabilityDay>,
        timesOfDay: Set<com.skillx.features.onboarding.domain.model.TimeOfDay>,
        formats: Set<com.skillx.features.onboarding.domain.model.LessonFormat>,
        durationMinutes: Int?,
        languages: List<com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry>
    ): AppResult<OnboardingProgress, AppError> {
        return try {
            val request = SaveAvailabilityRequestDto(
                days = days.map { it.name },
                timesOfDay = timesOfDay.map { it.name },
                lessonFormats = formats.map { it.name },
                preferredDurationMinutes = durationMinutes,
                languages = languages.map { LanguageProficiencyEntryDto(languageCode = it.languageCode, level = it.level.name) }
            )
            val response = onboardingApi.saveAvailability(request)
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to save availability."))
        }
    }

    override suspend fun complete(): AppResult<OnboardingProgress, AppError> {
        return try {
            val response = onboardingApi.completeOnboarding()
            if (response.status.isSuccess()) {
                val dto = response.body<OnboardingProgressDto>()
                AppResult.Success(OnboardingMapper.toDomain(dto))
            } else {
                val errorBody = try { response.body<com.skillx.network.error.ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to complete onboarding."))
        }
    }
}