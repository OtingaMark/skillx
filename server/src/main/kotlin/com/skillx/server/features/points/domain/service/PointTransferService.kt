package com.skillx.server.features.points.domain.service

import com.skillx.server.core.exceptions.InsufficientPointsException
import com.skillx.server.features.points.domain.model.PointTransaction
import com.skillx.server.features.points.domain.model.TransactionReason
import com.skillx.server.features.points.domain.repository.PointLedgerRepository
import com.skillx.server.features.points.domain.repository.PointRepository
import com.google.cloud.firestore.Transaction

/**
 * Domain service for transferring points between users.
 * Encapsulates the business rules for point transfers:
 * - Validates sufficient balance
 * - Atomically updates both user balances
 * - Creates a ledger entry for audit trail
 */
class PointTransferService(
    private val pointRepository: PointRepository,
    private val ledgerRepository: PointLedgerRepository
) {

    /**
     * Transfers points from one user to another within a transaction.
     * @param fromUserId The user sending points (learner)
     * @param toUserId The user receiving points (teacher)
     * @param amount Number of points to transfer (must be positive)
     * @param reason The reason for the transfer
     * @param lessonId Optional associated lesson ID
     * @param tx Firestore transaction (required for atomicity)
     * @return true if transfer succeeded, false if insufficient balance
     */
    suspend fun transferPoints(
        fromUserId: String,
        toUserId: String,
        amount: Int,
        reason: TransactionReason,
        lessonId: String? = null,
        tx: Transaction? = null
    ): Boolean {
        if (amount <= 0) return false

        val fromBalance = pointRepository.getBalance(fromUserId, tx)
        if (fromBalance < amount) {
            return false
        }

        // Deduct from sender
        pointRepository.adjustBalance(fromUserId, -amount, tx)
        // Credit to receiver
        pointRepository.adjustBalance(toUserId, amount, tx)

        // Write ledger entry
        val transaction = PointTransaction(
            id = java.util.UUID.randomUUID().toString(),
            fromUserId = fromUserId,
            toUserId = toUserId,
            amount = amount,
            reason = reason,
            lessonId = lessonId,
            timestamp = System.currentTimeMillis()
        )
        ledgerRepository.create(transaction, tx)

        return true
    }

    /**
     * Grants initial points to a new user (system credit).
     * @param userId The user to grant points to
     * @param amount Number of points to grant (default: 5)
     * @param reason Reason for the grant (default: INITIAL_GRANT)
     * @param tx Firestore transaction
     */
    suspend fun grantInitialPoints(
        userId: String,
        amount: Int = 5,
        reason: TransactionReason = TransactionReason.INITIAL_GRANT,
        tx: Transaction? = null
    ) {
        pointRepository.adjustBalance(userId, amount, tx)

        val transaction = PointTransaction(
            id = java.util.UUID.randomUUID().toString(),
            fromUserId = "SYSTEM",
            toUserId = userId,
            amount = amount,
            reason = reason,
            lessonId = null,
            timestamp = System.currentTimeMillis()
        )
        ledgerRepository.create(transaction, tx)
    }

    /**
     * Credits points for a verified purchase.
     * @param userId The user to credit
     * @param amount Number of points purchased
     * @param lessonId Optional associated lesson ID (for audit)
     * @param tx Firestore transaction
     */
    suspend fun creditPurchase(
        userId: String,
        amount: Int,
        lessonId: String? = null,
        tx: Transaction? = null
    ) {
        pointRepository.adjustBalance(userId, amount, tx)

        val transaction = PointTransaction(
            id = java.util.UUID.randomUUID().toString(),
            fromUserId = "SYSTEM",
            toUserId = userId,
            amount = amount,
            reason = TransactionReason.PURCHASE,
            lessonId = lessonId,
            timestamp = System.currentTimeMillis()
        )
        ledgerRepository.create(transaction, tx)
    }
}