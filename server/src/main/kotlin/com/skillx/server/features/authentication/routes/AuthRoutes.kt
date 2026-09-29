package com.skillx.server.features.authentication.routes

import com.skillx.server.configuration.OAuthConfig
import com.skillx.server.features.authentication.application.usecase.AuthenticateWithFederatedProviderUseCase
import com.skillx.server.features.authentication.application.usecase.LoginUserUseCase
import com.skillx.server.features.authentication.application.usecase.RegisterUserUseCase
import com.skillx.server.features.authentication.domain.model.AuthenticatedPrincipal
import com.skillx.server.features.authentication.domain.model.FederatedCredential
import com.skillx.server.features.authentication.domain.model.FederatedProvider
import com.skillx.server.core.exceptions.ValidationException
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class RegisterRequest(val name: String, val email: String, val password: String)
@Serializable data class LoginRequest(val email: String, val password: String)
@Serializable data class GoogleSignInRequest(
    val idToken: String? = null,
    val code: String? = null,
    val codeVerifier: String? = null
)
@Serializable data class LinkedInSignInRequest(val code: String)
@Serializable data class AuthResponse(val userId: String, val email: String, val token: String, val refreshToken: String) {
    companion object {
        fun fromPrincipal(principal: AuthenticatedPrincipal): AuthResponse =
            AuthResponse(principal.userId, principal.email, principal.token, principal.refreshToken)
    }
}

/**
 * Authentication HTTP routes.
 * Thin layer that delegates to use cases — no business logic here.
 */
fun Route.authRoutes() {
    val registerUseCase by inject<RegisterUserUseCase>()
    val loginUseCase by inject<LoginUserUseCase>()
    val federatedAuthUseCase by inject<com.skillx.server.features.authentication.application.usecase.AuthenticateWithFederatedProviderUseCase>()
    val oAuthConfig by inject<OAuthConfig>()

    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            val principal = registerUseCase(request.name, request.email, request.password)
            call.respond(HttpStatusCode.Created, AuthResponse.fromPrincipal(principal))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val principal: AuthenticatedPrincipal = loginUseCase(request.email, request.password)
            call.respond(AuthResponse.fromPrincipal(principal))
        }

        post("/google") {
            val request = call.receive<GoogleSignInRequest>()
            val credential = when {
                request.idToken != null -> FederatedCredential.GoogleIdToken(request.idToken)
                request.code != null && request.codeVerifier != null ->
                    FederatedCredential.GoogleAuthorizationCode(request.code, request.codeVerifier, oAuthConfig.google.iosRedirectUri)
                else -> throw ValidationException(
                    "INVALID_REQUEST",
                    "Provide either idToken (Android) or code and codeVerifier (iOS)."
                )
            }
            val principal = federatedAuthUseCase(FederatedProvider.GOOGLE, credential)
            call.respond(AuthResponse.fromPrincipal(principal))
        }

        post("/linkedin") {
            val request = call.receive<LinkedInSignInRequest>()
            val principal = federatedAuthUseCase(
                FederatedProvider.LINKEDIN,
                FederatedCredential.LinkedInAuthorizationCode(request.code, oAuthConfig.linkedin.redirectUri)
            )
            call.respond(AuthResponse.fromPrincipal(principal))
        }

        post("/logout") {
            // Client-side logout (token invalidation) - server doesn't maintain token blacklist
            // In production, consider a token blacklist/redis for immediate revocation
            call.respond(HttpStatusCode.OK, mapOf("message" to "Logged out."))
        }
    }
}