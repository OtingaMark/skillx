package com.skillx.server.features.messaging.interfaces.socket

import com.skillx.server.features.messaging.interfaces.http.response.ConversationResponse
import com.skillx.server.features.messaging.interfaces.http.response.MessageResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Server → client frames on the messaging WebSocket. Discriminated by a "type" field. */
@Serializable
sealed class MessagingSocketEvent {

    @Serializable
    @SerialName("message")
    data class MessageReceived(
        val conversation: ConversationResponse,
        val message: MessageResponse
    ) : MessagingSocketEvent()

    @Serializable
    @SerialName("conversation")
    data class ConversationUpdated(
        val conversation: ConversationResponse
    ) : MessagingSocketEvent()

    @Serializable
    @SerialName("presence")
    data class PresenceChanged(
        val userId: String,
        val online: Boolean
    ) : MessagingSocketEvent()

    fun encode(): String = SocketJson.encodeToString(serializer(), this)

    private companion object {
        val SocketJson = Json { classDiscriminator = "type"; encodeDefaults = true }
    }
}
