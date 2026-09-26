package com.skillx.network.serialization

import kotlinx.serialization.json.Json

/**
 * Shared Json configuration used across Ktor client and server.
 * Single source of truth for JSON serialization settings.
 */
val NetworkJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = false
    coerceInputValues = true
}
