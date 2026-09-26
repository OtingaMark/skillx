package com.skillx.core.error

/**
 * Validation errors: input does not satisfy business rules.
 */
sealed class ValidationError(override val message: String) : AppError {
    data object EmptyField : ValidationError("Please fill in all required fields.")
    data class InvalidEmail(override val message: String = "Please enter a valid email address.") : ValidationError(message)
    data class WeakPassword(override val message: String = "Password must be at least 6 characters.") : ValidationError(message)
    data class InvalidRating(override val message: String = "Please select a rating from 1 to 5.") : ValidationError(message)
    data class MissingReason(override val message: String = "Please select a reason.") : ValidationError(message)
    data class Custom(override val message: String) : ValidationError(message)
}
