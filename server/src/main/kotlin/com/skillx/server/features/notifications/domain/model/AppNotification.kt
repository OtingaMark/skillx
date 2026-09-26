package com.skillx.server.features.notifications.domain.model
data class AppNotification(val id: String, val userId: String, val title: String, val body: String, val type: String, val isRead: Boolean = false, val timestamp: Long)
