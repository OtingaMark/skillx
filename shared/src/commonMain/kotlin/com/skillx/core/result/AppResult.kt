package com.skillx.core.result

/**
 * A discriminated union that encapsulates a successful outcome with a value of type [T]
 * or a failure with an error of type [E].
 *
 * This is the canonical result type used across both client and server.
 * Domain code returns AppResult instead of throwing exceptions.
 */
sealed class AppResult<out T, out E> {

    data class Success<T>(val data: T) : AppResult<T, Nothing>()

    data class Error<E>(val error: E) : AppResult<Nothing, E>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> null
    }

    fun errorOrNull(): E? = when (this) {
        is Success -> null
        is Error -> error
    }

    inline fun <R> map(transform: (T) -> R): AppResult<R, E> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
    }

    inline fun <R> flatMap(transform: (T) -> AppResult<R, @UnsafeVariance E>): AppResult<R, E> = when (this) {
        is Success -> transform(data)
        is Error -> Error(error)
    }

    inline fun onSuccess(action: (T) -> Unit): AppResult<T, E> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (E) -> Unit): AppResult<T, E> {
        if (this is Error) action(error)
        return this
    }
}
