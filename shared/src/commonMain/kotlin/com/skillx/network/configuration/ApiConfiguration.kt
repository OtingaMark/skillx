package com.skillx.network.configuration

/**
 * API configuration for the Ktor client.
 * Centralizes base URL and timeout settings.
 */
data class ApiConfiguration(
    val baseUrl: String = DEFAULT_BASE_URL,
    val connectTimeoutMs: Long = 15_000,
    val requestTimeoutMs: Long = 30_000,
    val socketTimeoutMs: Long = 30_000
) {
    companion object {
        // Default to localhost for development — production URL configured via build config
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8080"
        const val API_VERSION = "v1"
    }

    fun apiUrl(path: String): String = "$baseUrl/api/$API_VERSION$path"
}
