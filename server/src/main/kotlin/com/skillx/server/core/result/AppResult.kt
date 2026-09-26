package com.skillx.server.core.result
sealed class AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>()
    data class Failure(val exception: Exception) : AppResult<Nothing>()
}
