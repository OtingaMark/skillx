package com.skillx.server.features.payments.interfaces.http.request
import kotlinx.serialization.Serializable
@Serializable data class VerifyPurchaseRequest(val productId: String, val transactionId: String, val receipt: String)
