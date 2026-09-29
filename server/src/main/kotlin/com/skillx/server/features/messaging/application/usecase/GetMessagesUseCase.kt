package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.features.messaging.domain.model.Message
import com.skillx.server.features.messaging.domain.model.Page
import com.skillx.server.features.messaging.domain.repository.ConversationRepository
import com.skillx.server.features.messaging.domain.repository.MessageRepository
import com.skillx.server.features.messaging.domain.service.ConversationAccess

class GetMessagesUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) {
    /** Newest-first page of messages; pass the returned cursor as [before] to load older ones. */
    suspend operator fun invoke(userId: String, conversationId: String, limit: Int?, before: String?): Page<Message> {
        ConversationAccess.requireParticipant(conversationRepository.findById(conversationId), userId)
        val pageSize = resolvePageSize(limit)
        val fetched = messageRepository.findByConversation(conversationId, pageSize + 1, before)
        val items = fetched.take(pageSize)
        val nextCursor = if (fetched.size > pageSize) items.last().id else null
        return Page(items, nextCursor)
    }
}
