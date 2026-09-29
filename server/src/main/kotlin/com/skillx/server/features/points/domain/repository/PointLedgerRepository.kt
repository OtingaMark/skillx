package com.skillx.server.features.points.domain.repository

import com.google.cloud.firestore.Transaction
import com.skillx.server.features.points.domain.model.PointTransaction

/**
 * Repository interface for point transaction ledger operations.
 * Provides audit trail for all point movements.
 */
interface PointLedgerRepository {

    /**
     * Records a point transaction in the ledger.
     */
    suspend fun create(transaction: PointTransaction, tx: Transaction? = null)

    /**
     * Retrieves the transaction history for a user (both sent and received).
     */
    suspend fun getHistory(userId: String): List<PointTransaction>

    /**
     * Retrieves a single transaction by ID.
     */
    suspend fun getById(id: String): PointTransaction?
}