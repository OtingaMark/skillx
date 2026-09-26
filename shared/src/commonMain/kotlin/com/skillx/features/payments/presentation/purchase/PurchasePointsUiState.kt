package com.skillx.features.payments.presentation.purchase
import com.skillx.features.payments.domain.model.PointPackage
data class PurchasePointsUiState(val packages: List<PointPackage> = emptyList(), val currentBalance: Int = 0, val isLoading: Boolean = true, val isPurchasing: Boolean = false, val purchaseSuccess: Boolean = false, val error: String = "")
