package com.skillx.server.features.points.domain.model

/**
 * Domain model representing a single point transaction in the ledger.
 * Required for audit trail of point movements.
 */
data class PointTransaction(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val amount: Int,
    val reason: TransactionReason,
    val lessonId: String? = null,
    val timestamp: Long
)

/**
 * Enum representing the reason for a point transaction.
 * Matches the shared module's TransactionReason for consistency.
 */
enum class TransactionReason {
    LESSON_COMPLETED,
    PURCHASE,
    INITIAL_GRANT,
    REFUND
}