package com.skillx.server.core.extensions

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import com.skillx.server.core.exceptions.AuthenticationException

/**
 * Extract the authenticated userId from the JWT principal.
 */
fun RoutingCall.userId(): String {
    return principal<JWTPrincipal>()
        ?.payload?.getClaim("userId")?.asString()
        ?: throw AuthenticationException("Invalid token.")
}
