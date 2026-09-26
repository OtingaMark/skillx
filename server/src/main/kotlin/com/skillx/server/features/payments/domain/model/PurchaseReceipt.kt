package com.skillx.server.features.payments.domain.model
data class PurchaseReceipt(val transactionId: String, val productId: String, val userId: String, val pointsCredited: Int)
