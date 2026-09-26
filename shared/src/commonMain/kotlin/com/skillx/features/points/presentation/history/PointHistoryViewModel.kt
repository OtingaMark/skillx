package com.skillx.features.points.presentation.history
import com.skillx.core.result.AppResult
import com.skillx.features.points.domain.usecase.LoadPointBalanceUseCase
import com.skillx.features.points.domain.usecase.ObservePointHistoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PointHistoryViewModel(private val loadBalance: LoadPointBalanceUseCase, private val observeHistory: ObservePointHistoryUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(PointHistoryUiState())
    val uiState: StateFlow<PointHistoryUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { _uiState.update { it.copy(isLoading = true) }; scope.launch {
        when (val b = loadBalance()) { is AppResult.Success -> _uiState.update { it.copy(currentBalance = b.data.points) }; is AppResult.Error -> {} }
        when (val h = observeHistory()) { is AppResult.Success -> _uiState.update { it.copy(transactions = h.data, isLoading = false) }; is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = h.error.message) } }
    } }
}
