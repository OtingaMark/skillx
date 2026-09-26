package com.skillx.features.payments.data.remote

import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

@Serializable
data class PointPackageDto(val productId: String, val points: Int, val formattedPrice: String)

@Serializable
data class VerifyPurchaseRequestDto(val productId: String, val transactionId: String, val receipt: String)

@Serializable
data class VerifyPurchaseResponseDto(val pointsCredited: Int)

class PaymentApi(private val client: HttpClient) {
    suspend fun getPackages(): HttpResponse = client.get("/api/v1/payments/packages")
    suspend fun verifyPurchase(request: VerifyPurchaseRequestDto): HttpResponse = client.post("/api/v1/payments/verify") { setBody(request) }
}
