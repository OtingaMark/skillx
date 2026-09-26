package com.skillx.server.infrastructure.notifications
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

class FirebaseAdminPushSender {
    fun sendToUser(fcmToken: String, title: String, body: String) {
        // TODO: Check if user has notifications enable 
        val message = Message.builder()
            .setToken(fcmToken)
            .setNotification(
                Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build()
            )
            .build()
        FirebaseMessaging.getInstance().send(message)
    }
}
