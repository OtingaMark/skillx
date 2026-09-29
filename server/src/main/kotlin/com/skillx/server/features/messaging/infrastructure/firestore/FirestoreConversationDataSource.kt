package com.skillx.server.features.messaging.infrastructure.firestore

import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Transaction
import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.repository.ConversationRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.infrastructure.firestore.await

/**
 * Conversations live in the top-level `conversations` collection.
 * Listing relies on the composite index (participantIds ARRAY_CONTAINS, lastMessageAt DESC).
 */
class FirestoreConversationDataSource(
    provider: FirestoreClientProvider
) : ConversationRepository {

    private val collection = provider.getFirestore().collection(COLLECTION)

    override suspend fun findById(id: String, tx: Transaction?): Conversation? {
        val ref = collection.document(id)
        val doc = if (tx != null) tx.get(ref).await() else ref.get().await()
        return if (doc.exists()) ConversationDocumentMapper.fromFirestore(doc) else null
    }

    override suspend fun save(conversation: Conversation, tx: Transaction?) {
        val ref = collection.document(conversation.id)
        val data = ConversationDocumentMapper.toFirestore(conversation)
        if (tx != null) tx.set(ref, data) else ref.set(data).await()
    }

    override suspend fun findByParticipant(userId: String, limit: Int, afterConversationId: String?): List<Conversation> {
        var query = collection
            .whereArrayContains("participantIds", userId)
            .orderBy("lastMessageAt", Query.Direction.DESCENDING)
            .limit(limit)
        if (afterConversationId != null) {
            val cursor = collection.document(afterConversationId).get().await()
            if (cursor.exists()) query = query.startAfter(cursor)
        }
        return query.get().await().documents.map { ConversationDocumentMapper.fromFirestore(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun findCounterpartIds(userId: String): Set<String> =
        collection
            .whereArrayContains("participantIds", userId)
            .select("participantIds")
            .get()
            .await()
            .documents
            .flatMap { (it.get("participantIds") as? List<String>).orEmpty() }
            .filterTo(mutableSetOf()) { it != userId }

    internal companion object {
        const val COLLECTION = "conversations"
    }
}
