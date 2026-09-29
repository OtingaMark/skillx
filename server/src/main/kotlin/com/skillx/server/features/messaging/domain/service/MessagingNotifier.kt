package com.skillx.server.features.messaging.domain.service

import com.skillx.server.features.messaging.domain.model.Conversation
import com.skillx.server.features.messaging.domain.model.Message

/**
 * Pushes committed messaging changes to connected participants. Called only after the
 * write has succeeded, so a push never announces data that isn't persisted.
 */
interface MessagingNotifier {
    suspend fun messageSent(conversation: Conversation, message: Message)

    suspend fun conversationUpdated(conversation: Conversation)
}
