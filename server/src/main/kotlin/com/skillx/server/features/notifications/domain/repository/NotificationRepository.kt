package com.skillx.server.features.notifications.domain.repository
interface NotificationRepository { suspend fun send(userId: String, title: String, body: String) }
