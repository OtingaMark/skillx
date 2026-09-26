package com.skillx.features.payments.data.remote

/**
 * RevenueCat SDK client wrapper for in-app purchases.
 * Platform-specific initialization required (Android: Play Billing, iOS: StoreKit).
 */
class RevenueCatClient {
    suspend fun initialize(apiKey: String) { /* RevenueCat SDK initialization */ }
    suspend fun getOfferings(): List<Map<String, Any>> { return emptyList() }
    suspend fun purchase(productId: String): String? { return null /* returns transaction ID */ }
    suspend fun restorePurchases(): List<String> { return emptyList() }
}
