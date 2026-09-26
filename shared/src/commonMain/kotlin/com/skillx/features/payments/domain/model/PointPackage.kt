package com.skillx.features.payments.domain.model

/**
 * Domain model for a purchasable point package.
 * Extracted from pointsForProduct() mapping in MainActivity.kt (L1675-1689).
 */
data class PointPackage(
    val productId: String,
    val points: Int,
    val formattedPrice: String
)

/**
 * Maps product IDs to point amounts.
 * Preserves the exact mapping from the original pointsForProduct() function.
 */
object PointPackageCatalog {
    private val catalog = mapOf(
        "skillx_points_5" to 5,
        "skillx_points_15" to 15,
        "skillx_points_30" to 30
    )

    fun pointsForProduct(productId: String): Int {
        return catalog[productId] ?: 0
    }
}
