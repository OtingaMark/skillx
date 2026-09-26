package com.skillx.core.error

/**
 * Conflict errors: operation would violate a uniqueness/state constraint.
 */
sealed class ConflictError(override val message: String) : AppError {
    data object DuplicateLessonRequest : ConflictError("You already have an active lesson request for this skill with this student.")
    data object AlreadyRated : ConflictError("You have already rated this lesson.")
    data object LessonAlreadyCompleted : ConflictError("This lesson has already been completed.")
    data class Custom(override val message: String) : ConflictError(message)
}
