package com.skillx.server.features.messaging.domain.service

import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.messaging.domain.model.Conversation

/**
 * The one authorization rule for messaging: only participants may see or act on a
 * conversation. Non-participants get the same "not found" as a missing conversation, so
 * conversation IDs can't be probed for existence.
 */
object ConversationAccess {
    fun requireParticipant(conversation: Conversation?, userId: String): Conversation {
        if (conversation == null || !conversation.isParticipant(userId)) {
            throw NotFoundException("Conversation not found.")
        }
        return conversation
    }
}
