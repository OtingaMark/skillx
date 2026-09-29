package com.skillx.server.features.notifications.infrastructure.firebase
import com.skillx.server.infrastructure.notifications.FirebaseAdminPushSender
class FirebaseAdminNotificationSender(private val pushSender: FirebaseAdminPushSender) { suspend fun send(fcmToken: String, title: String, body: String) = pushSender.sendToUser(fcmToken, title, body) }
