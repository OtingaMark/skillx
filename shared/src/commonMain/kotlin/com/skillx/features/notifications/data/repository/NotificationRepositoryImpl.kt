package com.skillx.features.notifications.data.repository
import com.skillx.features.notifications.domain.model.AppNotification
import com.skillx.features.notifications.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NotificationRepositoryImpl : NotificationRepository {
    override fun observeNotifications(): Flow<List<AppNotification>> = flowOf(emptyList())
    override suspend fun markAsRead(notificationId: String) {}
}
