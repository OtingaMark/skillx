package com.skillx.core.error

/**
 * Not found errors: requested resource does not exist.
 */
sealed class NotFoundError(override val message: String) : AppError {
    data class UserNotFound(val userId: String) : NotFoundError("User '$userId' was not found.")
    data class LessonNotFound(val lessonId: String) : NotFoundError("Lesson '$lessonId' was not found.")
    data class RatingNotFound(val ratingId: String) : NotFoundError("Rating '$ratingId' was not found.")
    data class Custom(override val message: String) : NotFoundError(message)
}
