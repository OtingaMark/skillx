package com.skillx.server.infrastructure.oauth

import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.mockk.every
import io.mockk.mockk
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OidcJwksVerifierTest {
    private val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
    private val publicKey = keyPair.public as RSAPublicKey
    private val privateKey = keyPair.private as RSAPrivateKey

    private val issuer = "https://accounts.google.com"
    private val audience = "test-client-id"
    private val keyId = "test-key-1"

    private fun jwkProviderReturning(key: RSAPublicKey): JwkProvider {
        val jwk = mockk<Jwk>()
        every { jwk.publicKey } returns key
        val provider = mockk<JwkProvider>()
        every { provider.get(any()) } returns jwk
        return provider
    }

    private fun signToken(
        subject: String = "user-123",
        aud: String = audience,
        iss: String = issuer,
        expiresAt: Date = Date(System.currentTimeMillis() + 60_000),
        signingKey: RSAPrivateKey = privateKey,
        signingPublicKey: RSAPublicKey = publicKey,
        claims: Map<String, String> = emptyMap()
    ): String {
        val builder = JWT.create()
            .withKeyId(keyId)
            .withSubject(subject)
            .withIssuer(iss)
            .withAudience(aud)
            .withExpiresAt(expiresAt)
        claims.forEach { (name, value) -> builder.withClaim(name, value) }
        return builder.sign(Algorithm.RSA256(signingPublicKey, signingKey))
    }

    @Test
    fun `valid token verifies and exposes claims`() {
        val verifier = OidcJwksVerifier(issuer, audience, jwkProviderReturning(publicKey))
        val token = signToken(claims = mapOf("email" to "user@example.com"))

        val decoded = verifier.verify(token)

        assertEquals("user-123", decoded.subject)
        assertEquals("user@example.com", decoded.getClaim("email").asString())
    }

    @Test
    fun `token with the wrong audience is rejected`() {
        val verifier = OidcJwksVerifier(issuer, audience, jwkProviderReturning(publicKey))
        val token = signToken(aud = "some-other-client-id")

        assertFailsWith<Exception> { verifier.verify(token) }
    }

    @Test
    fun `token signed with a key not present in the JWKS is rejected`() {
        val otherKeyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()
        // The provider only knows about `publicKey` for this keyId — the token below is
        // signed with a different, unrelated private key.
        val verifier = OidcJwksVerifier(issuer, audience, jwkProviderReturning(publicKey))
        val token = signToken(
            signingKey = otherKeyPair.private as RSAPrivateKey,
            signingPublicKey = otherKeyPair.public as RSAPublicKey
        )

        assertFailsWith<Exception> { verifier.verify(token) }
    }

    @Test
    fun `expired token is rejected`() {
        val verifier = OidcJwksVerifier(issuer, audience, jwkProviderReturning(publicKey))
        val token = signToken(expiresAt = Date(System.currentTimeMillis() - 60_000))

        assertFailsWith<Exception> { verifier.verify(token) }
    }
}
