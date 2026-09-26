package com.skillx.features.notifications.presentation.inbox
import kotlinx.coroutines.flow.*
import com.skillx.features.notifications.domain.model.AppNotification

class NotificationInboxViewModel {
    private val _notifications = MutableStateFlow<List<AppNotification>>(emptyList())
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()
}
