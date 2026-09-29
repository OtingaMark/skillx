package com.skillx.server.features.users.domain.repository

import com.google.cloud.firestore.Transaction
import com.skillx.server.features.users.domain.model.User

/**
 * Repository interface for user operations.
 */
interface UserRepository {

    /**
     * Finds a user by ID.
     */
    suspend fun findById(id: String, tx: Transaction? = null): User?

    /**
     * Finds a user by email.
     */
    suspend fun findByEmail(email: String): User?

    /**
     * Updates a user's data.
     */
    suspend fun update(id: String, data: Map<String, Any?>, tx: Transaction? = null)

    /**
     * Updates a user's skills.
     */
    suspend fun updateSkills(id: String, teachSkills: List<String>, learnSkills: List<String>, tx: Transaction? = null)
}