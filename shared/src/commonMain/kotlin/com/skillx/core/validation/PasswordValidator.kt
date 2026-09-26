package com.skillx.core.validation

/**
 * Pure Kotlin password validator.
 * Enforces the minimum 6-character rule from the original SignUpScreen.
 */
object PasswordValidator {

    const val MIN_LENGTH = 6

    fun isValid(password: String): Boolean {
        return password.length >= MIN_LENGTH
    }

    fun validate(password: String): PasswordValidationResult {
        return when {
            password.isBlank() -> PasswordValidationResult.Empty
            password.length < MIN_LENGTH -> PasswordValidationResult.TooShort(MIN_LENGTH)
            else -> PasswordValidationResult.Valid
        }
    }
}

sealed interface PasswordValidationResult {
    data object Valid : PasswordValidationResult
    data object Empty : PasswordValidationResult
    data class TooShort(val minLength: Int) : PasswordValidationResult
}
