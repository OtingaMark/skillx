package com.skillx.server.core.exceptions

open class AppException(override val message: String) : RuntimeException(message)
class AuthenticationException(message: String = "Authentication failed.") : AppException(message)
class AuthorizationException(message: String = "Access denied.") : AppException(message)
class ValidationException(val code: String, message: String) : AppException(message)
class NotFoundException(message: String = "Resource not found.") : AppException(message)
class ConflictException(val code: String, message: String) : AppException(message)
class InsufficientPointsException(message: String = "Insufficient points.") : AppException(message)
