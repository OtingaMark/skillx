package com.skillx.server.plugins

import com.skillx.server.core.exceptions.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val code: String, val message: String)

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, ErrorResponse("AUTHENTICATION_ERROR", cause.message ?: "Authentication failed."))
        }
        exception<AuthorizationException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("AUTHORIZATION_ERROR", cause.message ?: "Access denied."))
        }
        exception<ValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.code, cause.message ?: "Validation failed."))
        }
        exception<NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ErrorResponse("NOT_FOUND", cause.message ?: "Resource not found."))
        }
        exception<ConflictException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, ErrorResponse(cause.code, cause.message ?: "Conflict."))
        }
        exception<InsufficientPointsException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("INSUFFICIENT_POINTS", cause.message ?: "Insufficient points."))
        }
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled error", cause)
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred."))
        }
    }
}
