package com.skillx.core.validation

/**
 * Pure Kotlin email validator — no android.util dependency.
 * Validates email format using a standard regex pattern.
 */
object EmailValidator {

    private val EMAIL_PATTERN = Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
                "@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
    )

    fun isValid(email: String): Boolean {
        return email.trim().matches(EMAIL_PATTERN)
    }
}
