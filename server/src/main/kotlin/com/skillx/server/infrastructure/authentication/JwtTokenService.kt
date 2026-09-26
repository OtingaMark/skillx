package com.skillx.server.infrastructure.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.skillx.server.configuration.JwtConfig
import java.util.*

class JwtTokenService(private val config: JwtConfig) {
    fun generateToken(userId: String, email: String): String {
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("userId", userId)
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expirationMs))
            .sign(Algorithm.HMAC256(config.secret))
    }

    fun generateRefreshToken(userId: String): String {
        return JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("userId", userId)
            .withClaim("type", "refresh")
            .withExpiresAt(Date(System.currentTimeMillis() + config.expirationMs * 24))
            .sign(Algorithm.HMAC256(config.secret))
    }
}
