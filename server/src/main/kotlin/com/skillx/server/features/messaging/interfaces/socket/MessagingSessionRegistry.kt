package com.skillx.server.features.messaging.interfaces.socket

import com.skillx.server.features.messaging.domain.service.PresenceTracker
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * Open messaging WebSocket sessions per user (one user may have several devices).
 * In-memory, so presence and push reach only clients connected to this server instance.
 */
class MessagingSessionRegistry : PresenceTracker {

    private val sessions = ConcurrentHashMap<String, Set<WebSocketSession>>()

    /** Returns true when this is the user's first open session (they just came online). */
    fun register(userId: String, session: WebSocketSession): Boolean {
        var cameOnline = false
        sessions.compute(userId) { _, current ->
            cameOnline = current.isNullOrEmpty()
            current.orEmpty() + session
        }
        return cameOnline
    }

    /** Returns true when this was the user's last open session (they just went offline). */
    fun unregister(userId: String, session: WebSocketSession): Boolean {
        var wentOffline = false
        sessions.computeIfPresent(userId) { _, current ->
            val remaining = current - session
            wentOffline = remaining.isEmpty() && current.isNotEmpty()
            remaining.ifEmpty { null }
        }
        return wentOffline
    }

    override fun isOnline(userId: String): Boolean = !sessions[userId].isNullOrEmpty()

    /** Best-effort push to every session of [userId]; a session that fails is cleaned up when its handler exits. */
    suspend fun sendTo(userId: String, payload: String) {
        sessions[userId].orEmpty().forEach { session ->
            runCatching { session.send(Frame.Text(payload)) }
        }
    }
}
