package com.skillx.features.users.presentation.home
import com.skillx.core.result.AppResult
import com.skillx.features.users.domain.usecase.LoadCurrentUserUseCase
import com.skillx.features.points.domain.usecase.LoadPointBalanceUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
    private val loadPointBalanceUseCase: LoadPointBalanceUseCase,
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        _uiState.update { it.copy(isLoading = true, error = "") }
        scope.launch {
            when (val result = loadCurrentUserUseCase()) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false, user = result.data) }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.error.message) }
            }
            when (val balance = loadPointBalanceUseCase()) {
                is AppResult.Success -> _uiState.update { it.copy(pointBalance = balance.data.points) }
                is AppResult.Error -> { /* non-critical */ }
            }
        }
    }
}
