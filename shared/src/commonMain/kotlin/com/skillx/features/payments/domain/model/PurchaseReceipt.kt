package com.skillx.features.payments.domain.model

/**
 * Domain model representing a completed purchase receipt.
 * Used for server-side purchase verification.
 */
data class PurchaseReceipt(
    val userId: String,
    val productId: String,
    val transactionId: String,
    val pointsCredited: Int,
    val timestamp: Long
)
