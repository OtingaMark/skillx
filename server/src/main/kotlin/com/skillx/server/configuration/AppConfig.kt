package com.skillx.server.configuration
data class AppConfig(val environment: String, val port: Int, val host: String) {
    companion object { fun fromEnvironment() = AppConfig(System.getenv("APP_ENV") ?: "development", (System.getenv("PORT") ?: "8080").toInt(), System.getenv("HOST") ?: "0.0.0.0") }
    val isDevelopment get() = environment == "development"
    val isProduction get() = environment == "production"
}
