package com.skillx.server.configuration

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val expirationMs: Long = 3600_000 // 1 hour
) {
    companion object {
        fun fromEnvironment(): JwtConfig {
            return JwtConfig(
                secret = System.getenv("JWT_SECRET") ?: "skillx-dev-secret-change-in-production",
                issuer = System.getenv("JWT_ISSUER") ?: "http://0.0.0.0:8080/",
                audience = System.getenv("JWT_AUDIENCE") ?: "http://0.0.0.0:8080/api",
                realm = "SkillX API"
            )
        }
    }
}
