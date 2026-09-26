package com.skillx.features.matching.presentation.detail
import com.skillx.features.matching.domain.model.SkillMatch
import kotlinx.coroutines.flow.*

class MatchProfileViewModel(match: SkillMatch) {
    private val _uiState = MutableStateFlow(MatchProfileUiState(match = match))
    val uiState: StateFlow<MatchProfileUiState> = _uiState.asStateFlow()
}
