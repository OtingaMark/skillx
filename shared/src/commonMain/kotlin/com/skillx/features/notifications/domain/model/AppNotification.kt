package com.skillx.features.notifications.domain.model

/**
 * Domain model for an in-app notification.
 */
data class AppNotification(
    val id: String,
    val title: String,
    val body: String,
    val type: NotificationType,
    val relatedId: String? = null,
    val isRead: Boolean = false,
    val timestamp: Long
)

enum class NotificationType {
    LESSON_REQUESTED,
    LESSON_ACCEPTED,
    LESSON_COMPLETED,
    RATING_RECEIVED,
    POINTS_CREDITED,
    GENERAL
}
