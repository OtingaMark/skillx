package com.skillx.server.features.authentication.domain.model

import com.skillx.server.core.exceptions.ValidationException

/** The single server-side password rule, shared by registration and password reset. */
object PasswordPolicy {
    const val MIN_LENGTH = 6

    fun requireValid(password: String) {
        if (password.length < MIN_LENGTH) {
            throw ValidationException("WEAK_PASSWORD", "Password must be at least $MIN_LENGTH characters.")
        }
    }
}
