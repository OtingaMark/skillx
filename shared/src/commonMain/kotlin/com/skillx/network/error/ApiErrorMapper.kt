package com.skillx.network.error

import com.skillx.core.error.AppError
import com.skillx.core.error.AuthenticationError
import com.skillx.core.error.AuthorizationError
import com.skillx.core.error.ConflictError
import com.skillx.core.error.NetworkError
import com.skillx.core.error.NotFoundError
import com.skillx.core.error.InsufficientPointsError
import com.skillx.core.error.ValidationError
import kotlinx.serialization.Serializable

/**
 * Standard error response from the server API.
 */
@Serializable
data class ApiErrorResponse(
    val code: String,
    val message: String
)

/**
 * Maps HTTP status codes and server error responses to domain AppError types.
 */
object ApiErrorMapper {

    fun fromHttpStatus(statusCode: Int, errorResponse: ApiErrorResponse?): AppError {
        val message = errorResponse?.message ?: "An unexpected error occurred."
        val code = errorResponse?.code ?: ""

        return when (statusCode) {
            400 -> mapBadRequest(code, message)
            401 -> AuthenticationError.TokenExpired
            403 -> AuthorizationError.Forbidden
            404 -> NotFoundError.Custom(message)
            409 -> mapConflict(code, message)
            422 -> ValidationError.Custom(message)
            429 -> NetworkError.HttpError(429, "Too many requests. Please try again later.")
            in 500..599 -> NetworkError.HttpError(statusCode, "Server error. Please try again later.")
            else -> NetworkError.HttpError(statusCode, message)
        }
    }

    private fun mapBadRequest(code: String, message: String): AppError {
        return when (code) {
            "INVALID_EMAIL" -> ValidationError.InvalidEmail(message)
            "WEAK_PASSWORD" -> ValidationError.WeakPassword(message)
            "INVALID_CREDENTIALS" -> AuthenticationError.InvalidCredentials
            "INSUFFICIENT_POINTS" -> InsufficientPointsError(message = message)
            else -> ValidationError.Custom(message)
        }
    }

    private fun mapConflict(code: String, message: String): AppError {
        return when (code) {
            "DUPLICATE_LESSON_REQUEST" -> ConflictError.DuplicateLessonRequest
            "ALREADY_RATED" -> ConflictError.AlreadyRated
            "LESSON_ALREADY_COMPLETED" -> ConflictError.LessonAlreadyCompleted
            else -> ConflictError.Custom(message)
        }
    }
}
