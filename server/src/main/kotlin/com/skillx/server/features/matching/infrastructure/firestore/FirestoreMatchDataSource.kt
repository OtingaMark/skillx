package com.skillx.server.features.matching.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestoreMatchDataSource(private val provider: FirestoreClientProvider) { fun getAllUsers() = provider.getFirestore().collection("users").get().get() }
