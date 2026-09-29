package com.skillx.server.features.users.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Transaction
import com.skillx.server.infrastructure.firestore.await
import com.skillx.server.features.users.domain.model.User
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider

/**
 * Firestore implementation of UserRepository.
 * Handles all user persistence operations.
 */
class FirestoreUserDataSource(
    private val provider: FirestoreClientProvider
) : UserRepository {

    private val db = provider.getFirestore()
    private val collection = db.collection("users")

    override suspend fun findById(id: String, tx: Transaction?): User? {
        val doc = if (tx != null) {
            tx.get(collection.document(id)).await()
        } else {
            collection.document(id).get().await()
        }
        return if (doc.exists()) UserDocumentMapper.fromFirestore(doc) else null
    }

    override suspend fun findByEmail(email: String): User? {
        val query = collection.whereEqualTo("email", email).limit(1).get().await()
        return query.documents.firstOrNull()?.let { UserDocumentMapper.fromFirestore(it) }
    }

    override suspend fun update(id: String, data: Map<String, Any?>, tx: Transaction?) {
        if (tx != null) {
            tx.set(collection.document(id), data, com.google.cloud.firestore.SetOptions.merge())
        } else {
            collection.document(id).set(data, com.google.cloud.firestore.SetOptions.merge()).await()
        }
    }

    override suspend fun updateSkills(id: String, teachSkills: List<String>, learnSkills: List<String>, tx: Transaction?) {
        val data = mapOf("teachSkills" to teachSkills, "learnSkills" to learnSkills)
        update(id, data, tx)
    }
}