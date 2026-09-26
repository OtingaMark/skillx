package com.skillx.server.infrastructure.firestore

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.skillx.server.configuration.FirebaseConfig
import java.io.FileInputStream

class FirestoreClientProvider(private val config: FirebaseConfig) {
    private var app: FirebaseApp? = null

    fun getFirestore(): Firestore {
        if (app == null) {
            val serviceAccount = FileInputStream(config.serviceAccountPath)
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
            app = FirebaseApp.initializeApp(options)
        }
        return FirestoreClient.getFirestore()
    }
}
