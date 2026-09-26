package com.skillx.core.error

/**
 * Network-level errors: timeouts, connectivity, HTTP failures.
 */
sealed class NetworkError(override val message: String) : AppError {
    data object NoConnection : NetworkError("No internet connection available.")
    data object Timeout : NetworkError("The request timed out.")
    data class HttpError(val code: Int, override val message: String) : NetworkError(message)
    data class Unknown(override val message: String) : NetworkError(message)
}
