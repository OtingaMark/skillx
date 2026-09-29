package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.messaging.domain.model.Message
import com.skillx.server.features.messaging.domain.model.MessagingLimits
import com.skillx.server.features.messaging.domain.repository.ConversationRepository
import com.skillx.server.features.messaging.domain.repository.MessageRepository
import com.skillx.server.features.messaging.domain.service.ConversationAccess
import com.skillx.server.features.messaging.domain.service.MessagingNotifier
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner
import java.util.UUID

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val transactionRunner: FirestoreTransactionRunner,
    private val notifier: MessagingNotifier,
    private val clock: () -> Long = System::currentTimeMillis
) {
    suspend operator fun invoke(userId: String, conversationId: String, text: String): Message {
        val body = text.trim()
        if (body.isEmpty()) {
            throw ValidationException("EMPTY_MESSAGE", "Message cannot be empty.")
        }
        if (body.length > MessagingLimits.MAX_MESSAGE_LENGTH) {
            throw ValidationException(
                "MESSAGE_TOO_LONG",
                "Message cannot exceed ${MessagingLimits.MAX_MESSAGE_LENGTH} characters."
            )
        }

        // Generated outside the transaction so a Firestore retry re-writes the same message
        // instead of producing a duplicate.
        val message = Message(
            id = UUID.randomUUID().toString(),
            conversationId = conversationId,
            senderId = userId,
            text = body,
            sentAt = clock()
        )

        // The conversation read, message write, and unread-count update commit atomically.
        val updated = transactionRunner.runTransaction { tx ->
            val conversation = ConversationAccess.requireParticipant(
                conversationRepository.findById(conversationId, tx), userId
            )
            val withMessage = conversation.withMessage(message)
            messageRepository.create(message, tx)
            conversationRepository.save(withMessage, tx)
            withMessage
        }

        notifier.messageSent(updated, message)
        return message
    }
}
