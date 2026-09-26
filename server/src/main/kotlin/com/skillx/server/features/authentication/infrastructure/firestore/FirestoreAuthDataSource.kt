package com.skillx.server.features.authentication.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider

class FirestoreAuthDataSource(private val provider: FirestoreClientProvider) {
    suspend fun findByEmail(email: String): Map<String, Any>? { val docs = provider.getFirestore().collection("users").whereEqualTo("email", email).get().get(); return if (docs.isEmpty) null else docs.documents.first().data?.plus("id" to docs.documents.first().id) }
    suspend fun createUser(id: String, data: Map<String, Any>) { provider.getFirestore().collection("users").document(id).set(data).get() }
}
