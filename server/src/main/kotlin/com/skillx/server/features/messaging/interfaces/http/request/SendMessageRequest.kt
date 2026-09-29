package com.skillx.server.features.messaging.interfaces.http.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(val text: String)
