package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.repository.ConversationRepository
import com.skillx.server.features.messaging.domain.service.ConversationAccess
import com.skillx.server.features.messaging.domain.service.MessagingNotifier
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

class MarkConversationReadUseCase(
    private val conversationRepository: ConversationRepository,
    private val transactionRunner: FirestoreTransactionRunner,
    private val notifier: MessagingNotifier
) {
    suspend operator fun invoke(userId: String, conversationId: String): Conversation {
        val (conversation, changed) = transactionRunner.runTransaction { tx ->
            val current = ConversationAccess.requireParticipant(
                conversationRepository.findById(conversationId, tx), userId
            )
            if (current.unreadFor(userId) == 0) {
                current to false
            } else {
                val updated = current.markedReadBy(userId)
                conversationRepository.save(updated, tx)
                updated to true
            }
        }
        // Lets the reader's other open sessions clear the same unread badge.
        if (changed) notifier.conversationUpdated(conversation)
        return conversation
    }
}
