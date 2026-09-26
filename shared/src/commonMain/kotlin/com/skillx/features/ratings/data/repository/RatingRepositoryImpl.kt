package com.skillx.features.ratings.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.ratings.data.remote.RatingApi
import com.skillx.features.ratings.data.remote.dto.*
import com.skillx.features.ratings.domain.model.Rating
import com.skillx.features.ratings.domain.model.RatingSummary
import com.skillx.features.ratings.domain.repository.RatingRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*

class RatingRepositoryImpl(private val api: RatingApi) : RatingRepository {
    override suspend fun submitRating(lessonId: String, ratedUserId: String, rating: Int, comment: String): AppResult<Rating, AppError> {
        return try {
            val response = api.submitRating(SubmitRatingRequestDto(lessonId, ratedUserId, rating, comment))
            if (response.status.isSuccess()) {
                val dto = response.body<RatingDto>()
                AppResult.Success(Rating(dto.id, dto.lessonId, dto.raterId, dto.ratedUserId, dto.ratedUserName, dto.rating, dto.comment, dto.timestamp))
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to submit rating.")) }
    }

    override suspend fun getRatingSummary(userId: String): AppResult<RatingSummary, AppError> {
        return try {
            val response = api.getRatingSummary(userId)
            if (response.status.isSuccess()) {
                val dto = response.body<RatingSummaryDto>()
                AppResult.Success(RatingSummary(dto.userId, dto.averageRating, dto.totalRatings))
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load rating summary.")) }
    }

    override suspend fun hasAlreadyRated(lessonId: String): AppResult<Boolean, AppError> {
        return try {
            val response = api.hasAlreadyRated(lessonId)
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<AlreadyRatedDto>().alreadyRated)
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to check rating.")) }
    }
}
