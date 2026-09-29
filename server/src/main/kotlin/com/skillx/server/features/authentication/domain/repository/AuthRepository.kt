package com.skillx.server.features.authentication.domain.repository

import com.skillx.server.features.authentication.domain.model.AuthenticatedPrincipal
import com.skillx.server.features.authentication.domain.model.FederatedIdentity
import com.skillx.server.features.authentication.domain.model.FederatedProvider

/**
 * Repository interface for authentication operations.
 * Defines the contract for user credential storage and retrieval.
 */
interface AuthRepository {

    /**
     * Finds a user by email.
     * @param email Normalized email address
     * @return User data including ID, email, and password hash, or null if not found
     */
    suspend fun findByEmail(email: String): UserCredentials?

    /**
     * Checks if a user exists with the given email.
     * @param email Normalized email address
     * @return true if a user exists with this email
     */
    suspend fun existsByEmail(email: String): Boolean

    /**
     * Creates a new user account.
     * @param name User's display name
     * @param email Normalized email address
     * @param passwordHash Argon2id or bcrypt hash of the password
     * @return The newly created user's ID
     */
    suspend fun createUser(name: String, email: String, passwordHash: String): String

    /**
     * Updates a user's password hash (used for rehashing on login).
     * @param userId The user's ID
     * @param newPasswordHash The new Argon2id hash
     */
    suspend fun updatePasswordHash(userId: String, newPasswordHash: String)

    /**
     * Finds a user by federated identity.
     * @param provider The federated provider
     * @param externalId The external ID from the provider
     * @return User data or null if not found
     */
    suspend fun findByFederatedIdentity(provider: com.skillx.server.features.authentication.domain.model.FederatedProvider, externalId: String): UserData?

    /**
     * Links a federated identity to an existing user.
     * @param userId The user's ID
     * @param provider The federated provider
     * @param externalId The external ID from the provider
     */
    suspend fun linkFederatedIdentity(userId: String, provider: com.skillx.server.features.authentication.domain.model.FederatedProvider, externalId: String)

    /**
     * Creates a new user from federated identity.
     * @param identity The federated identity
     * @return The newly created user's ID
     */
    suspend fun createFromFederatedIdentity(identity: com.skillx.server.features.authentication.domain.model.FederatedIdentity): String

    /**
     * Data class for user credentials returned by findByEmail.
     */
    data class UserCredentials(
        val id: String,
        val email: String,
        val passwordHash: String
    )

    /**
     * Data class for user data returned by findByFederatedIdentity.
     */
    data class UserData(
        val id: String,
        val email: String
    )
}