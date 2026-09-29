package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.model.ConversationId
import com.skillx.server.features.messaging.domain.model.MessagingLimits
import com.skillx.server.features.messaging.domain.repository.ConversationRepository
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * Returns the existing conversation between two users, or creates it.
 * The deterministic conversation ID plus the transactional read-then-create make this
 * idempotent under concurrent requests.
 */
class StartConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository,
    private val transactionRunner: FirestoreTransactionRunner,
    private val clock: () -> Long = System::currentTimeMillis
) {
    data class Result(val conversation: Conversation, val created: Boolean)

    suspend operator fun invoke(userId: String, recipientId: String, subject: String?): Result {
        if (recipientId == userId) {
            throw ValidationException("CANNOT_MESSAGE_SELF", "You can't start a conversation with yourself.")
        }
        val normalizedSubject = subject?.trim()?.takeIf { it.isNotEmpty() }
        if (normalizedSubject != null && normalizedSubject.length > MessagingLimits.MAX_SUBJECT_LENGTH) {
            throw ValidationException(
                "SUBJECT_TOO_LONG",
                "Subject cannot exceed ${MessagingLimits.MAX_SUBJECT_LENGTH} characters."
            )
        }

        val sender = userRepository.findById(userId) ?: throw NotFoundException("User not found.")
        val recipient = userRepository.findById(recipientId) ?: throw NotFoundException("User not found.")
        val conversationId = ConversationId.forParticipants(userId, recipientId)

        return transactionRunner.runTransaction { tx ->
            val existing = conversationRepository.findById(conversationId, tx)
            if (existing != null) {
                Result(existing, created = false)
            } else {
                val now = clock()
                val conversation = Conversation(
                    id = conversationId,
                    participantIds = listOf(userId, recipientId).sorted(),
                    participantNames = mapOf(sender.id to sender.name, recipient.id to recipient.name),
                    subject = normalizedSubject,
                    lastMessage = null,
                    lastMessageAt = now,
                    unreadCounts = mapOf(userId to 0, recipientId to 0),
                    createdAt = now
                )
                conversationRepository.save(conversation, tx)
                Result(conversation, created = true)
            }
        }
    }
}
