package com.skillx.network.configuration

/**
 * API configuration for the Ktor client.
 * Centralizes base URL and timeout settings.
 *
 * baseUrl is deliberately required, not defaulted here — it varies per platform and per
 * build (emulator vs. physical device vs. production) and belongs in each platform's own
 * configuration source (Android BuildConfig, iOS Info.plist), not as a literal in shared code.
 */
data class ApiConfiguration(
    val baseUrl: String,
    val connectTimeoutMs: Long = 15_000,
    val requestTimeoutMs: Long = 30_000,
    val socketTimeoutMs: Long = 30_000
) {
    companion object {
        const val API_VERSION = "v1"
    }

    fun apiUrl(path: String): String = "$baseUrl/api/$API_VERSION$path"
}
