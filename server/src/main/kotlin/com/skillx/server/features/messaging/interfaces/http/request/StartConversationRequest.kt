package com.skillx.server.features.messaging.interfaces.http.request

import kotlinx.serialization.Serializable

@Serializable
data class StartConversationRequest(
    val recipientId: String,
    val subject: String? = null
)
