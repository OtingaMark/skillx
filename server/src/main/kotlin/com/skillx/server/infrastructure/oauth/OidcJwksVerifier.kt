package com.skillx.server.infrastructure.oauth

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * Shared JWKS verifier for OIDC providers.
 * Both Google and LinkedIn use RS256/JWKS-signed OIDC tokens.
 */
class OidcJwksVerifier(
    private val issuer: String,
    private val audience: String,
    private val jwkProvider: JwkProvider
) {
    // JwkProviderBuilder(String) treats the string as an Auth0 "domain" and appends
    // .well-known/jwks.json — wrong here, since jwksUrl is already the full JWKS endpoint.
    constructor(issuer: String, audience: String, jwksUrl: String) : this(
        issuer,
        audience,
        JwkProviderBuilder(URL(jwksUrl))
            .cached(10, 24, TimeUnit.HOURS)
            .rateLimited(10, 1, TimeUnit.MINUTES)
            .build()
    )

    @Throws(Exception::class)
    fun verify(idToken: String): com.auth0.jwt.interfaces.DecodedJWT {
        return try {
            val unverified = JWT.decode(idToken)
            val jwk = jwkProvider.get(unverified.keyId)
            val algorithm = Algorithm.RSA256(jwk.publicKey as java.security.interfaces.RSAPublicKey, null)
            val verifier: JWTVerifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .withAudience(audience)
                .build()
            verifier.verify(idToken)
        } catch (e: Exception) {
            throw RuntimeException("Invalid or expired $issuer token: ${e.message}", e)
        }
    }
}