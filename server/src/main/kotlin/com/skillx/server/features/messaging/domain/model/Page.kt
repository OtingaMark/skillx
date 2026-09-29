package com.skillx.server.features.messaging.domain.model

/** A page of results plus the cursor to request the next one (null when there's no more). */
data class Page<T>(
    val items: List<T>,
    val nextCursor: String?
)
