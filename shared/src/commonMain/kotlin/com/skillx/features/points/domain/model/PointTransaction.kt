package com.skillx.features.points.domain.model

/**
 * Domain model representing a single point transaction in the ledger.
 * Required by Section 7 of the architecture doc:
 * "Add a pointTransactions ledger collection so balances are auditable."
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

enum class TransactionReason {
    LESSON_COMPLETED,
    PURCHASE,
    INITIAL_GRANT,
    REFUND
}
