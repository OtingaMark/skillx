package com.skillx.features.users.presentation.view
import com.skillx.core.result.AppResult
import com.skillx.features.users.domain.usecase.LoadCurrentUserUseCase
import com.skillx.features.ratings.domain.usecase.LoadRatingSummaryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(private val loadCurrentUserUseCase: LoadCurrentUserUseCase, private val loadRatingSummaryUseCase: LoadRatingSummaryUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { _uiState.update { it.copy(isLoading = true) }; scope.launch {
        when (val r = loadCurrentUserUseCase()) { is AppResult.Success -> { _uiState.update { it.copy(user = r.data, isLoading = false) }; loadRating(r.data.id) }; is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = r.error.message) } }
    } }
    private fun loadRating(userId: String) { scope.launch { when (val r = loadRatingSummaryUseCase(userId)) { is AppResult.Success -> _uiState.update { it.copy(averageRating = r.data.averageRating, totalRatings = r.data.totalRatings) }; is AppResult.Error -> {} } } }
}
