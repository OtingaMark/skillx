package com.skillx.server.features.messaging.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.model.MessagePreview

internal object ConversationDocumentMapper {

    fun toFirestore(conversation: Conversation): Map<String, Any?> = mapOf(
        "participantIds" to conversation.participantIds,
        "participantNames" to conversation.participantNames,
        "subject" to conversation.subject,
        "lastMessage" to conversation.lastMessage?.let {
            mapOf("text" to it.text, "senderId" to it.senderId, "sentAt" to it.sentAt)
        },
        "lastMessageAt" to conversation.lastMessageAt,
        "unreadCounts" to conversation.unreadCounts,
        "createdAt" to conversation.createdAt
    )

    @Suppress("UNCHECKED_CAST")
    fun fromFirestore(doc: DocumentSnapshot): Conversation {
        val lastMessage = (doc.get("lastMessage") as? Map<String, Any?>)?.let {
            MessagePreview(
                text = it["text"] as? String ?: "",
                senderId = it["senderId"] as? String ?: "",
                sentAt = (it["sentAt"] as? Number)?.toLong() ?: 0L
            )
        }
        return Conversation(
            id = doc.id,
            participantIds = doc.get("participantIds") as? List<String> ?: emptyList(),
            participantNames = doc.get("participantNames") as? Map<String, String> ?: emptyMap(),
            subject = doc.getString("subject"),
            lastMessage = lastMessage,
            lastMessageAt = doc.getLong("lastMessageAt") ?: 0L,
            unreadCounts = (doc.get("unreadCounts") as? Map<String, Number>)
                ?.mapValues { it.value.toInt() } ?: emptyMap(),
            createdAt = doc.getLong("createdAt") ?: 0L
        )
    }
}
