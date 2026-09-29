package com.skillx.server.features.messaging.interfaces.http.response

import kotlinx.serialization.Serializable

/**
 * Every response is shaped for the requesting user ("viewer"): the other participant,
 * the viewer's own unread count, and whether each message was sent by the viewer.
 * Other participants' unread counts are never exposed.
 */
@Serializable
data class MessagePreviewResponse(
    val text: String,
    val sentAt: Long,
    val sentByMe: Boolean
)

@Serializable
data class ConversationResponse(
    val id: String,
    val otherParticipantId: String,
    val otherParticipantName: String,
    val subject: String?,
    val lastMessage: MessagePreviewResponse?,
    val lastMessageAt: Long,
    val unreadCount: Int,
    val otherParticipantOnline: Boolean
)

@Serializable
data class ConversationPageResponse(
    val conversations: List<ConversationResponse>,
    val nextCursor: String?
)

@Serializable
data class MessageResponse(
    val id: String,
    val conversationId: String,
    val text: String,
    val sentAt: Long,
    val sentByMe: Boolean
)

@Serializable
data class MessagePageResponse(
    val messages: List<MessageResponse>,
    val nextCursor: String?
)
