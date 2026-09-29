package com.skillx.server.features.messaging.domain.model

/** Denormalized copy of a conversation's latest message, so listing never reads the messages subcollection. */
data class MessagePreview(
    val text: String,
    val senderId: String,
    val sentAt: Long
)
