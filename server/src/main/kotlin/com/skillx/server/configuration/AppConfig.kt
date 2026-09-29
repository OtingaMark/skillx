package com.skillx.server.configuration

/**
 * Application configuration loaded from environment variables.
 */
data class AppConfig(
    val environment: String,
    val port: Int,
    val host: String,
    val corsAllowedOrigins: List<String> = listOf("*")
) {
    companion object {
        fun fromEnvironment(): AppConfig {
            val env = System.getenv("APP_ENV") ?: "development"
            val corsOrigins = System.getenv("CORS_ALLOWED_ORIGINS")
                ?.split(",")
                ?.map { it.trim() }
                ?: listOf("*")

            return AppConfig(
                environment = env,
                port = (System.getenv("PORT") ?: "8080").toInt(),
                host = System.getenv("HOST") ?: "0.0.0.0",
                corsAllowedOrigins = corsOrigins
            )
        }
    }

    val isDevelopment get() = environment == "development"
    val isProduction get() = environment == "production"
}