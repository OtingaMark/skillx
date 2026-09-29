package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.repository.ConversationRepository
import com.skillx.server.features.messaging.domain.service.ConversationAccess

class GetConversationUseCase(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(userId: String, conversationId: String): Conversation =
        ConversationAccess.requireParticipant(conversationRepository.findById(conversationId), userId)
}
