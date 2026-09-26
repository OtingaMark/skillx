package com.skillx.server.core.error
class ConflictException(val code: String, message: String) : AppException(message)
