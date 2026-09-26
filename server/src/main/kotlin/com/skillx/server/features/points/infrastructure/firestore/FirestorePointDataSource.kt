package com.skillx.server.features.points.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestorePointDataSource(private val provider: FirestoreClientProvider) { fun getBalance(userId: String) = provider.getFirestore().collection("users").document(userId).get().get() }
