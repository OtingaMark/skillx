package com.skillx.server.infrastructure.notifications

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Firebase Admin SDK push notification sender.
 * Sends push notifications to users via FCM tokens stored in Firestore.
 * Runs on the server only — never ships Admin SDK credentials to clients.
 */
class FirebaseAdminPushSender {

    /**
     * Sends a push notification to a specific user's device.
     * @param fcmToken The user's FCM registration token
     * @param title Notification title
     * @param body Notification body text
     * @param data Optional custom data payload
     * @return true if sent successfully, false otherwise
     */
    suspend fun sendToUser(
        fcmToken: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val message = Message.builder()
                .setToken(fcmToken)
                .setNotification(
                    Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .putAllData(data)
                .build()

            FirebaseMessaging.getInstance().send(message)
            true
        } catch (e: FirebaseMessagingException) {
            // Log the error but don't crash - notification delivery is best-effort
            // In production, you might want to retry or queue for later
            false
        }
    }

    /**
     * Sends a push notification to multiple users.
     * @param fcmTokens List of FCM registration tokens
     * @param title Notification title
     * @param body Notification body text
     * @param data Optional custom data payload
     * @return Number of successfully sent notifications
     */
    suspend fun sendToMultiple(
        fcmTokens: List<String>,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ): Int = withContext(Dispatchers.IO) {
        var successCount = 0
        for (token in fcmTokens) {
            if (sendToUser(token, title, body, data)) {
                successCount++
            }
        }
        successCount
    }

    /**
     * Sends a silent data-only push (for background sync, etc.).
     * @param fcmToken The user's FCM registration token
     * @param data Custom data payload
     * @return true if sent successfully, false otherwise
     */
    suspend fun sendDataMessage(
        fcmToken: String,
        data: Map<String, String>
    ): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val message = Message.builder()
                .setToken(fcmToken)
                .putAllData(data)
                .build()

            FirebaseMessaging.getInstance().send(message)
            true
        } catch (e: FirebaseMessagingException) {
            false
        }
    }
}