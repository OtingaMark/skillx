package com.skillx.features.points.presentation.history

import com.skillx.features.points.domain.model.PointTransaction

/**
 * UI state for the point history screen.
 * Displays the transaction ledger and current balance.
 */
data class PointHistoryUiState(
    /** All point transactions involving the current user (credits and debits) */
    val transactions: List<PointTransaction> = emptyList(),
    /** Current point balance */
    val currentBalance: Int = 0,
    /** True while history is being fetched */
    val isLoading: Boolean = true,
    /** Error message if loading failed */
    val error: String = ""
)