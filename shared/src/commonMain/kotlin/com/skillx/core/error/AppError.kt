package com.skillx.core.error

/**
 * Base sealed interface for all application errors.
 * Every feature-specific error extends this, ensuring exhaustive handling.
 */
sealed interface AppError {
    val message: String
}
