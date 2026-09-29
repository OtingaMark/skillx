package com.skillx.server.features.points.domain.repository

import com.google.cloud.firestore.Transaction

/**
 * Repository interface for user point balance operations.
 * All methods support optional Firestore Transaction for atomicity.
 */
interface PointRepository {

    /**
     * Gets the current point balance for a user.
     */
    suspend fun getBalance(userId: String, tx: Transaction? = null): Int

    /**
     * Sets the point balance for a user (absolute value).
     */
    suspend fun setBalance(userId: String, balance: Int, tx: Transaction? = null)

    /**
     * Adjusts the point balance by a delta (positive or negative).
     */
    suspend fun adjustBalance(userId: String, delta: Int, tx: Transaction? = null)
}