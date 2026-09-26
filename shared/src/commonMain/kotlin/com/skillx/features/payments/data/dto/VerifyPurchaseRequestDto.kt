package com.skillx.features.payments.data.dto
import kotlinx.serialization.Serializable
@Serializable data class VerifyPurchaseRequestDto(val productId: String, val transactionId: String, val receipt: String)
