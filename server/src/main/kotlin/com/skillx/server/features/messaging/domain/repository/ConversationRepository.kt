package com.skillx.server.features.messaging.domain.repository

import com.google.cloud.firestore.Transaction
import com.skillx.server.features.messaging.domain.model.Conversation

interface ConversationRepository {
    suspend fun findById(id: String, tx: Transaction? = null): Conversation?

    suspend fun save(conversation: Conversation, tx: Transaction? = null)

    /**
     * Conversations the user participates in, most recently active first.
     * Returns up to [limit] items starting after [afterConversationId] (exclusive), or from
     * the beginning when it's null.
     */
    suspend fun findByParticipant(userId: String, limit: Int, afterConversationId: String?): List<Conversation>

    /** IDs of every user who shares a conversation with [userId]. */
    suspend fun findCounterpartIds(userId: String): Set<String>
}
