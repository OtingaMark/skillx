package com.skillx.features.notifications.data.repository
import com.skillx.features.notifications.domain.model.AppNotification
import com.skillx.features.notifications.domain.repository.NotificationRepository
import com.skillx.core.result.AppResult
import com.skillx.core.error.AppError

class NotificationRepositoryImpl : NotificationRepository {
    override suspend fun getNotifications(): AppResult<List<AppNotification>, AppError> = AppResult.Success(emptyList())
    override suspend fun markAsRead(notificationId: String): AppResult<Unit, AppError> = AppResult.Success(Unit)
}
