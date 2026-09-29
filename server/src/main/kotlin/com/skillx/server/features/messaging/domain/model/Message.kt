package com.skillx.server.features.messaging.domain.model

data class Message(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val text: String,
    val sentAt: Long
)
