package com.skillx.server.features.authentication.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.FieldValue
import com.skillx.server.features.authentication.domain.model.FederatedProvider
import com.skillx.server.features.authentication.domain.model.FederatedProviderEntry
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.infrastructure.firestore.await

/**
 * Firestore implementation of AuthRepository.
 * Handles user credential storage and retrieval from the 'users' collection.
 */
class FirestoreAuthDataSource(
    private val provider: FirestoreClientProvider
) : AuthRepository {

    private val db = provider.getFirestore()
    private val usersCollection = db.collection("users")

    override suspend fun findByEmail(email: String): AuthRepository.UserCredentials? {
        val query = usersCollection.whereEqualTo("email", email).limit(1).get().await()
        return query.documents.firstOrNull()?.let { doc ->
            AuthRepository.UserCredentials(
                id = doc.id,
                email = doc.getString("email") ?: "",
                passwordHash = doc.getString("passwordHash") ?: ""
            )
        }
    }

    override suspend fun existsByEmail(email: String): Boolean {
        val query = usersCollection.whereEqualTo("email", email).limit(1).get().await()
        return !query.isEmpty()
    }

    override suspend fun createUser(name: String, email: String, passwordHash: String): String {
        val userId = java.util.UUID.randomUUID().toString()
        val userData = mapOf(
            "name" to name,
            "email" to email,
            "passwordHash" to passwordHash,
            "teachSkills" to emptyList<String>(),
            "learnSkills" to emptyList<String>(),
            "points" to 5,
            "onboardingCompleted" to false,
            "federatedProviders" to emptyList<FederatedProviderEntry>()
        )
        usersCollection.document(userId).set(userData).await()
        return userId
    }

    override suspend fun updatePasswordHash(userId: String, newPasswordHash: String) {
        usersCollection.document(userId).update("passwordHash", newPasswordHash).await()
    }

    override suspend fun findByFederatedIdentity(
        provider: com.skillx.server.features.authentication.domain.model.FederatedProvider,
        externalId: String
    ): AuthRepository.UserData? {
        // whereArrayContains, not whereEqualTo — a user can have more than one linked
        // provider, and an exact-array match would stop matching as soon as they do.
        val query = usersCollection
            .whereArrayContains("federatedProviders", FederatedProviderEntry(provider, externalId))
            .limit(1)
            .get()
            .await()
        return query.documents.firstOrNull()?.let { doc ->
            AuthRepository.UserData(id = doc.id, email = doc.getString("email") ?: "")
        }
    }

    override suspend fun linkFederatedIdentity(
        userId: String,
        provider: com.skillx.server.features.authentication.domain.model.FederatedProvider,
        externalId: String
    ) {
        // Manually append to the federatedProviders array to avoid FieldValue.arrayUnion type issues
        val userDoc = usersCollection.document(userId).get().await()
        val currentProviders = if (userDoc.exists()) {
            val providers = userDoc.get("federatedProviders")
            if (providers is List<*>) providers.toMutableList() else mutableListOf()
        } else {
            mutableListOf()
        }
        val entry = FederatedProviderEntry(provider, externalId)
        currentProviders.add(entry)
        usersCollection.document(userId).update("federatedProviders", currentProviders).await()
    }

    override suspend fun createFromFederatedIdentity(identity: com.skillx.server.features.authentication.domain.model.FederatedIdentity): String {
        val userId = java.util.UUID.randomUUID().toString()
        val userData = mutableMapOf(
            "name" to (identity.displayName ?: "User"),
            "email" to (identity.email ?: ""),
            "teachSkills" to emptyList<String>(),
            "learnSkills" to emptyList<String>(),
            "points" to 5,
            "onboardingCompleted" to false,
            "federatedProviders" to listOf(
                FederatedProviderEntry(identity.provider, identity.externalId)
            )
        )
        usersCollection.document(userId).set(userData).await()
        return userId
    }
}