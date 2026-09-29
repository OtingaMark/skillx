package com.skillx.server.features.messaging.domain.repository

import com.google.cloud.firestore.Transaction
import com.skillx.server.features.messaging.domain.model.Message

interface MessageRepository {
    suspend fun create(message: Message, tx: Transaction? = null)

    /**
     * Messages in a conversation, newest first. Returns up to [limit] items older than
     * [beforeMessageId] (exclusive), or the newest ones when it's null.
     */
    suspend fun findByConversation(conversationId: String, limit: Int, beforeMessageId: String?): List<Message>
}
