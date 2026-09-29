package com.skillx.server.features.messaging.infrastructure.firestore

import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Transaction
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.messaging.domain.model.Message
import com.skillx.server.features.messaging.domain.repository.MessageRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.infrastructure.firestore.await

/** Messages live in `conversations/{conversationId}/messages`, so they're scoped to their conversation by path. */
class FirestoreMessageDataSource(
    provider: FirestoreClientProvider
) : MessageRepository {

    private val conversations = provider.getFirestore().collection(FirestoreConversationDataSource.COLLECTION)

    private fun messagesOf(conversationId: String) =
        conversations.document(conversationId).collection(MESSAGES)

    override suspend fun create(message: Message, tx: Transaction?) {
        val ref = messagesOf(message.conversationId).document(message.id)
        val data = MessageDocumentMapper.toFirestore(message)
        if (tx != null) tx.set(ref, data) else ref.set(data).await()
    }

    override suspend fun findByConversation(conversationId: String, limit: Int, beforeMessageId: String?): List<Message> {
        val messages = messagesOf(conversationId)
        var query = messages.orderBy("sentAt", Query.Direction.DESCENDING).limit(limit)
        if (beforeMessageId != null) {
            val cursor = messages.document(beforeMessageId).get().await()
            if (!cursor.exists()) throw ValidationException("INVALID_CURSOR", "Invalid pagination cursor.")
            query = query.startAfter(cursor)
        }
        return query.get().await().documents.map { MessageDocumentMapper.fromFirestore(it) }
    }

    private companion object {
        const val MESSAGES = "messages"
    }
}
