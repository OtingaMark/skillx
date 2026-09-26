package com.skillx.server.features.users.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import kotlinx.coroutines.tasks.await

class FirestoreUserDataSource(private val provider: FirestoreClientProvider) {
    suspend fun getUser(id: String): DocumentSnapshot {
        return provider.getFirestore()
            .collection("users")
            .document(id)
            .get()
            .await()
    }
}
