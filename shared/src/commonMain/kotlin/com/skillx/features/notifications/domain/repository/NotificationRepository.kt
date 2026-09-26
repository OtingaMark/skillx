package com.skillx.features.notifications.domain.repository

import com.skillx.features.notifications.domain.model.AppNotification
import kotlinx.coroutines.flow.Flow

/**
 * Notification repository interface.
 */
interface NotificationRepository {
    fun observeNotifications(): Flow<List<AppNotification>>
    suspend fun markAsRead(notificationId: String)
}
