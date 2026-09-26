package com.skillx.core.error

/**
 * Authentication errors: invalid credentials, expired tokens, etc.
 */
sealed class AuthenticationError(override val message: String) : AppError {
    data object InvalidCredentials : AuthenticationError("Invalid email or password.")
    data object TokenExpired : AuthenticationError("Your session has expired. Please log in again.")
    data object NotAuthenticated : AuthenticationError("You must be logged in to perform this action.")
    data class AccountCreationFailed(override val message: String) : AuthenticationError(message)
}
