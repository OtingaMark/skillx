package com.skillx.server.features.search.application.usecase

import com.skillx.server.core.exceptions.ValidationException

/** Shared input rules for the search endpoints — one place, so every search validates the same way. */
internal object SearchQuery {
    const val MAX_QUERY_LENGTH = 100
    const val DEFAULT_LIMIT = 20
    const val MAX_LIMIT = 50

    fun normalizeQuery(query: String): String {
        val trimmed = query.trim()
        if (trimmed.length > MAX_QUERY_LENGTH) {
            throw ValidationException("QUERY_TOO_LONG", "Search query must be at most $MAX_QUERY_LENGTH characters.")
        }
        return trimmed.lowercase()
    }

    fun clampLimit(limit: Int?): Int = (limit ?: DEFAULT_LIMIT).coerceIn(1, MAX_LIMIT)
}
