package com.skillx.server.features.messaging.domain.model

/**
 * A one-to-one conversation. Participant list is the authorization boundary for every
 * messaging operation; unread counts are tracked per participant.
 */
data class Conversation(
    val id: String,
    val participantIds: List<String>,
    val participantNames: Map<String, String>,
    val subject: String?,
    val lastMessage: MessagePreview?,
    val lastMessageAt: Long,
    val unreadCounts: Map<String, Int>,
    val createdAt: Long
) {
    fun isParticipant(userId: String): Boolean = userId in participantIds

    fun otherParticipantId(userId: String): String = participantIds.first { it != userId }

    fun unreadFor(userId: String): Int = unreadCounts[userId] ?: 0

    fun withMessage(message: Message): Conversation = copy(
        lastMessage = MessagePreview(message.text, message.senderId, message.sentAt),
        lastMessageAt = message.sentAt,
        unreadCounts = participantIds.associateWith { participant ->
            if (participant == message.senderId) 0 else unreadFor(participant) + 1
        }
    )

    fun markedReadBy(userId: String): Conversation = copy(
        unreadCounts = unreadCounts + (userId to 0)
    )
}
