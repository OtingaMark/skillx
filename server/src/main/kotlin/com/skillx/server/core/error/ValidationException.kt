package com.skillx.server.core.error
class ValidationException(val code: String, message: String) : AppException(message)
