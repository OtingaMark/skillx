package com.skillx.server.core.error

open class AppException(override val message: String, override val cause: Throwable? = null) : RuntimeException(message, cause)
