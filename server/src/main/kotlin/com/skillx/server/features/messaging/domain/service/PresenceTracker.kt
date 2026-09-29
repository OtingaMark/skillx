package com.skillx.server.features.messaging.domain.service

/** A user is online while they hold at least one open messaging WebSocket session. */
interface PresenceTracker {
    fun isOnline(userId: String): Boolean
}
