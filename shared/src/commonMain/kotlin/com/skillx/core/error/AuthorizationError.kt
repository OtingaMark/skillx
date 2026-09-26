package com.skillx.core.error

/**
 * Authorization errors: user lacks permission for the requested operation.
 */
sealed class AuthorizationError(override val message: String) : AppError {
    data object Forbidden : AuthorizationError("You do not have permission to perform this action.")
    data class NotParticipant(override val message: String) : AuthorizationError(message)
    data class NotTeacher(override val message: String) : AuthorizationError(message)
}
