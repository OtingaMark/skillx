package com.skillx.features.points.presentation.history
import com.skillx.features.points.domain.model.PointTransaction
data class PointHistoryUiState(val transactions: List<PointTransaction> = emptyList(), val currentBalance: Int = 0, val isLoading: Boolean = true, val error: String = "")
