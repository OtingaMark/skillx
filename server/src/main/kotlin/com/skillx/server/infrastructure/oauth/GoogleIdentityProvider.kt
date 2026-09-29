package com.skillx.server.infrastructure.oauth

import com.skillx.server.configuration.OAuthConfig
import com.skillx.server.core.exceptions.AuthenticationException
import com.skillx.server.features.authentication.domain.model.FederatedCredential
import com.skillx.server.features.authentication.domain.model.FederatedIdentity
import com.skillx.server.features.authentication.domain.model.FederatedProvider
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProvider

class GoogleIdentityProvider(
    config: OAuthConfig,
    private val tokenExchangeClient: GoogleTokenExchangeClient,
    private val verifier: OidcJwksVerifier = OidcJwksVerifier(
        issuer = config.google.issuer,
        audience = config.google.clientId,
        jwksUrl = config.google.jwksUrl
    )
) : FederatedIdentityProvider {
    override val provider = FederatedProvider.GOOGLE

    override suspend fun resolveIdentity(credential: FederatedCredential): FederatedIdentity {
        val decoded = try {
            // Android hands over a Credential Manager ID token directly; iOS has no equivalent
            // SDK, so it authenticates via a browser PKCE code exchange instead. Both converge
            // on the same verified ID token below.
            val idToken = when (credential) {
                is FederatedCredential.GoogleIdToken -> credential.idToken
                is FederatedCredential.GoogleAuthorizationCode -> tokenExchangeClient.exchangeCodeForIdToken(
                    code = credential.code,
                    codeVerifier = credential.codeVerifier,
                    redirectUri = credential.redirectUri
                )
                else -> throw IllegalArgumentException(
                    "GoogleIdentityProvider only accepts a Google ID token or authorization code."
                )
            }
            verifier.verify(idToken)
        } catch (e: Exception) {
            throw AuthenticationException("Invalid or expired Google token: ${e.message}")
        }

        return FederatedIdentity(
            provider = FederatedProvider.GOOGLE,
            externalId = decoded.subject,
            email = decoded.getClaim("email").asString(),
            emailVerified = decoded.getClaim("email_verified").asBoolean() ?: false,
            displayName = decoded.getClaim("name").asString(),
            avatarUrl = decoded.getClaim("picture").asString()
        )
    }
}