package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.model.Page
import com.skillx.server.features.messaging.domain.repository.ConversationRepository

class ListConversationsUseCase(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(userId: String, limit: Int?, cursor: String?): Page<Conversation> {
        if (cursor != null) {
            val cursorConversation = conversationRepository.findById(cursor)
            if (cursorConversation == null || !cursorConversation.isParticipant(userId)) {
                throw ValidationException("INVALID_CURSOR", "Invalid pagination cursor.")
            }
        }
        val pageSize = resolvePageSize(limit)
        val fetched = conversationRepository.findByParticipant(userId, pageSize + 1, cursor)
        val items = fetched.take(pageSize)
        val nextCursor = if (fetched.size > pageSize) items.last().id else null
        return Page(items, nextCursor)
    }
}
