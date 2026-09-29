package com.skillx.server.infrastructure.oauth

import com.auth0.jwt.interfaces.DecodedJWT
import com.skillx.server.configuration.OAuthConfig
import com.skillx.server.core.exceptions.AuthenticationException
import com.skillx.server.features.authentication.domain.model.FederatedCredential
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GoogleIdentityProviderTest {
    private val config = OAuthConfig(
        google = OAuthConfig.GoogleOAuthSettings(clientId = "test-client", iosRedirectUri = "skillx://oauth/google/callback"),
        linkedin = OAuthConfig.LinkedInOAuthSettings(clientId = "li-id", clientSecret = "li-secret", redirectUri = "skillx://oauth/linkedin/callback")
    )

    private fun decodedJwt(subject: String, email: String?, emailVerified: Boolean?): DecodedJWT {
        val decoded = mockk<DecodedJWT>()
        every { decoded.subject } returns subject
        every { decoded.getClaim("email").asString() } returns email
        every { decoded.getClaim("email_verified").asBoolean() } returns emailVerified
        every { decoded.getClaim("name").asString() } returns "Test User"
        every { decoded.getClaim("picture").asString() } returns null
        return decoded
    }

    @Test
    fun `resolves identity from a direct Google ID token`() = runBlocking {
        val verifier = mockk<OidcJwksVerifier>()
        every { verifier.verify("valid-id-token") } returns decodedJwt("google-user-1", "user@example.com", true)

        val provider = GoogleIdentityProvider(config, mockk(), verifier)
        val identity = provider.resolveIdentity(FederatedCredential.GoogleIdToken("valid-id-token"))

        assertEquals("google-user-1", identity.externalId)
        assertEquals("user@example.com", identity.email)
        assertEquals(true, identity.emailVerified)
    }

    @Test
    fun `exchanges an authorization code before verifying, for the iOS PKCE flow`() = runBlocking {
        val verifier = mockk<OidcJwksVerifier>()
        every { verifier.verify("exchanged-id-token") } returns decodedJwt("google-user-2", "user2@example.com", true)

        val exchangeClient = mockk<GoogleTokenExchangeClient>()
        coEvery {
            exchangeClient.exchangeCodeForIdToken("auth-code", "verifier-123", config.google.iosRedirectUri)
        } returns "exchanged-id-token"

        val provider = GoogleIdentityProvider(config, exchangeClient, verifier)
        val identity = provider.resolveIdentity(
            FederatedCredential.GoogleAuthorizationCode("auth-code", "verifier-123", config.google.iosRedirectUri)
        )

        assertEquals("google-user-2", identity.externalId)
        assertEquals("user2@example.com", identity.email)
    }

    @Test
    fun `wraps a verification failure as AuthenticationException`() = runBlocking {
        val verifier = mockk<OidcJwksVerifier>()
        every { verifier.verify("bad-token") } throws RuntimeException("signature mismatch")

        val provider = GoogleIdentityProvider(config, mockk(), verifier)

        assertFailsWith<AuthenticationException> {
            provider.resolveIdentity(FederatedCredential.GoogleIdToken("bad-token"))
        }
        Unit
    }
}
