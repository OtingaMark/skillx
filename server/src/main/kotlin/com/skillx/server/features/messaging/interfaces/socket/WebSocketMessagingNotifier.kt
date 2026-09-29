package com.skillx.server.features.messaging.interfaces.socket

import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.model.Message
import com.skillx.server.features.messaging.domain.service.MessagingNotifier
import com.skillx.server.features.messaging.interfaces.http.response.MessagingResponseMapper

/** Delivers committed messaging changes to each participant, shaped for that participant. */
class WebSocketMessagingNotifier(
    private val registry: MessagingSessionRegistry,
    private val mapper: MessagingResponseMapper
) : MessagingNotifier {

    override suspend fun messageSent(conversation: Conversation, message: Message) {
        conversation.participantIds.forEach { participant ->
            val event = MessagingSocketEvent.MessageReceived(
                conversation = mapper.conversation(conversation, participant),
                message = mapper.message(message, participant)
            )
            registry.sendTo(participant, event.encode())
        }
    }

    override suspend fun conversationUpdated(conversation: Conversation) {
        conversation.participantIds.forEach { participant ->
            val event = MessagingSocketEvent.ConversationUpdated(mapper.conversation(conversation, participant))
            registry.sendTo(participant, event.encode())
        }
    }
}
