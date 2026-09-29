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
import kotlin.test.assertNull

class LinkedInIdentityProviderTest {
    private val config = OAuthConfig(
        google = OAuthConfig.GoogleOAuthSettings(clientId = "g-id", iosRedirectUri = "skillx://oauth/google/callback"),
        linkedin = OAuthConfig.LinkedInOAuthSettings(
            clientId = "linkedin-client",
            clientSecret = "linkedin-secret",
            redirectUri = "skillx://oauth/linkedin/callback"
        )
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
    fun `exchanges the code, then resolves identity from the returned ID token`() = runBlocking {
        val verifier = mockk<OidcJwksVerifier>()
        every { verifier.verify("exchanged-token") } returns decodedJwt("linkedin-user-1", "user@example.com", true)

        val exchangeClient = mockk<LinkedInTokenExchangeClient>()
        coEvery { exchangeClient.exchangeCodeForIdToken("code-1", config.linkedin.redirectUri) } returns "exchanged-token"

        val provider = LinkedInIdentityProvider(config, exchangeClient, verifier)
        val identity = provider.resolveIdentity(
            FederatedCredential.LinkedInAuthorizationCode("code-1", config.linkedin.redirectUri)
        )

        assertEquals("linkedin-user-1", identity.externalId)
        assertEquals("user@example.com", identity.email)
    }

    @Test
    fun `a LinkedIn identity with no email claim resolves with a null email, without crashing`() = runBlocking {
        val verifier = mockk<OidcJwksVerifier>()
        every { verifier.verify("exchanged-token-2") } returns decodedJwt("linkedin-user-2", null, null)

        val exchangeClient = mockk<LinkedInTokenExchangeClient>()
        coEvery { exchangeClient.exchangeCodeForIdToken("code-2", config.linkedin.redirectUri) } returns "exchanged-token-2"

        val provider = LinkedInIdentityProvider(config, exchangeClient, verifier)
        val identity = provider.resolveIdentity(
            FederatedCredential.LinkedInAuthorizationCode("code-2", config.linkedin.redirectUri)
        )

        assertNull(identity.email)
        assertEquals(false, identity.emailVerified)
    }

    @Test
    fun `wraps an exchange failure as AuthenticationException`() = runBlocking {
        val exchangeClient = mockk<LinkedInTokenExchangeClient>()
        coEvery { exchangeClient.exchangeCodeForIdToken(any(), any()) } throws RuntimeException("network error")

        val provider = LinkedInIdentityProvider(config, exchangeClient, mockk())

        assertFailsWith<AuthenticationException> {
            provider.resolveIdentity(FederatedCredential.LinkedInAuthorizationCode("bad-code", config.linkedin.redirectUri))
        }
        Unit
    }
}
