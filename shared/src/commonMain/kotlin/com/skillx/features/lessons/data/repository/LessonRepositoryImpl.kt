package com.skillx.features.lessons.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.data.remote.LessonApi
import com.skillx.features.lessons.data.remote.dto.CreateLessonRequestDto
import com.skillx.features.lessons.data.remote.dto.LessonRequestDto
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.model.LessonStatus
import com.skillx.features.lessons.domain.repository.LessonRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*

class LessonRepositoryImpl(
    private val lessonApi: LessonApi
) : LessonRepository {

    private fun LessonRequestDto.toDomain() = LessonRequest(
        id = id, requesterId = requesterId, teacherId = teacherId,
        requesterName = requesterName, teacherName = teacherName,
        skill = skill, status = LessonStatus.fromString(status)
    )

    override suspend fun createLessonRequest(teacherId: String, skill: String): AppResult<LessonRequest, AppError> {
        return try {
            val response = lessonApi.createLessonRequest(CreateLessonRequestDto(teacherId, skill))
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<LessonRequestDto>().toDomain())
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to create lesson request."))
        }
    }

    override suspend fun getMyLessonRequests(): AppResult<List<LessonRequest>, AppError> {
        return try {
            val response = lessonApi.getMyLessonRequests()
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<List<LessonRequestDto>>().map { it.toDomain() })
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load lessons."))
        }
    }

    override suspend fun acceptLessonRequest(lessonId: String): AppResult<LessonRequest, AppError> {
        return try {
            val response = lessonApi.acceptLessonRequest(lessonId)
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<LessonRequestDto>().toDomain())
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to accept lesson."))
        }
    }

    override suspend fun completeLesson(lessonId: String): AppResult<LessonRequest, AppError> {
        return try {
            val response = lessonApi.completeLesson(lessonId)
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<LessonRequestDto>().toDomain())
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to complete lesson."))
        }
    }
}
