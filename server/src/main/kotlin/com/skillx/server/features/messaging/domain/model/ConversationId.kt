package com.skillx.server.features.messaging.domain.model

/**
 * Deterministic ID for the one conversation between two users — the same pair always maps
 * to the same document, so two concurrent "start conversation" requests can't create duplicates.
 */
object ConversationId {
    fun forParticipants(first: String, second: String): String =
        listOf(first, second).sorted().joinToString(separator = "_")
}
