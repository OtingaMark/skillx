package com.skillx.server.features.messaging.interfaces.http.response

import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.model.Message
import com.skillx.server.features.messaging.domain.model.Page
import com.skillx.server.features.messaging.domain.service.PresenceTracker

class MessagingResponseMapper(
    private val presenceTracker: PresenceTracker
) {
    fun conversation(conversation: Conversation, viewerId: String): ConversationResponse {
        val otherId = conversation.otherParticipantId(viewerId)
        return ConversationResponse(
            id = conversation.id,
            otherParticipantId = otherId,
            otherParticipantName = conversation.participantNames[otherId].orEmpty(),
            subject = conversation.subject,
            lastMessage = conversation.lastMessage?.let {
                MessagePreviewResponse(it.text, it.sentAt, sentByMe = it.senderId == viewerId)
            },
            lastMessageAt = conversation.lastMessageAt,
            unreadCount = conversation.unreadFor(viewerId),
            otherParticipantOnline = presenceTracker.isOnline(otherId)
        )
    }

    fun conversationPage(page: Page<Conversation>, viewerId: String): ConversationPageResponse =
        ConversationPageResponse(page.items.map { conversation(it, viewerId) }, page.nextCursor)

    fun message(message: Message, viewerId: String): MessageResponse =
        MessageResponse(
            id = message.id,
            conversationId = message.conversationId,
            text = message.text,
            sentAt = message.sentAt,
            sentByMe = message.senderId == viewerId
        )

    fun messagePage(page: Page<Message>, viewerId: String): MessagePageResponse =
        MessagePageResponse(page.items.map { message(it, viewerId) }, page.nextCursor)
}
