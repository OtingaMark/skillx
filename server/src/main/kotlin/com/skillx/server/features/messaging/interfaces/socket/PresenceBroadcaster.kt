package com.skillx.server.features.messaging.interfaces.socket

import com.skillx.server.features.messaging.domain.repository.ConversationRepository

/** Tells everyone who shares a conversation with a user that the user came online or went offline. */
class PresenceBroadcaster(
    private val conversationRepository: ConversationRepository,
    private val registry: MessagingSessionRegistry
) {
    suspend fun broadcast(userId: String, online: Boolean) {
        val payload = MessagingSocketEvent.PresenceChanged(userId, online).encode()
        conversationRepository.findCounterpartIds(userId)
            .filter { registry.isOnline(it) }
            .forEach { registry.sendTo(it, payload) }
    }
}
