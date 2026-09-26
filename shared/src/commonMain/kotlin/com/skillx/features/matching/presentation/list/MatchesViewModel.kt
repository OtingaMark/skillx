package com.skillx.features.matching.presentation.list
import com.skillx.core.result.AppResult
import com.skillx.features.matching.domain.usecase.FindSkillMatchesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MatchesViewModel(private val findSkillMatchesUseCase: FindSkillMatchesUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { _uiState.update { it.copy(isLoading = true) }; scope.launch { when (val r = findSkillMatchesUseCase()) { is AppResult.Success -> _uiState.update { it.copy(matches = r.data, isLoading = false) }; is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = r.error.message) } } } }
    fun onSearchQueryChanged(q: String) { _uiState.update { it.copy(searchQuery = q) } }
    fun onCategorySelected(c: String) { _uiState.update { it.copy(selectedCategory = c) } }
}
