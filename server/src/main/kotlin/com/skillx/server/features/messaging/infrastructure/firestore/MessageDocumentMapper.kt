package com.skillx.server.features.messaging.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.skillx.server.features.messaging.domain.model.Message

internal object MessageDocumentMapper {

    fun toFirestore(message: Message): Map<String, Any> = mapOf(
        "conversationId" to message.conversationId,
        "senderId" to message.senderId,
        "text" to message.text,
        "sentAt" to message.sentAt
    )

    fun fromFirestore(doc: DocumentSnapshot): Message = Message(
        id = doc.id,
        conversationId = doc.getString("conversationId") ?: "",
        senderId = doc.getString("senderId") ?: "",
        text = doc.getString("text") ?: "",
        sentAt = doc.getLong("sentAt") ?: 0L
    )
}
