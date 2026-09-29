package com.skillx.server.infrastructure.oauth

import com.skillx.server.configuration.OAuthConfig
import com.skillx.server.core.exceptions.AuthenticationException
import com.skillx.server.features.authentication.domain.model.FederatedCredential
import com.skillx.server.features.authentication.domain.model.FederatedIdentity
import com.skillx.server.features.authentication.domain.model.FederatedProvider
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProvider

class LinkedInIdentityProvider(
    config: OAuthConfig,
    private val tokenExchangeClient: LinkedInTokenExchangeClient,
    private val verifier: OidcJwksVerifier = OidcJwksVerifier(
        issuer = config.linkedin.issuer,
        audience = config.linkedin.clientId,
        jwksUrl = config.linkedin.jwksUrl
    )
) : FederatedIdentityProvider {
    override val provider = FederatedProvider.LINKEDIN

    override suspend fun resolveIdentity(credential: FederatedCredential): FederatedIdentity {
        require(credential is FederatedCredential.LinkedInAuthorizationCode) {
            "LinkedInIdentityProvider only accepts a LinkedIn authorization code."
        }

        val decoded = try {
            val idToken = tokenExchangeClient.exchangeCodeForIdToken(credential.code, credential.redirectUri)
            verifier.verify(idToken)
        } catch (e: Exception) {
            throw AuthenticationException("Invalid or expired LinkedIn token: ${e.message}")
        }

        return FederatedIdentity(
            provider = FederatedProvider.LINKEDIN,
            externalId = decoded.subject,
            email = decoded.getClaim("email").asString(), // nullable — LinkedIn doesn't guarantee email
            emailVerified = decoded.getClaim("email_verified").asBoolean() ?: false,
            displayName = decoded.getClaim("name").asString(),
            avatarUrl = decoded.getClaim("picture").asString()
        )
    }
}