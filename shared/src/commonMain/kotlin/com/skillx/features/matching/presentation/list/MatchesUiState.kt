package com.skillx.features.matching.presentation.list
import com.skillx.features.matching.domain.model.SkillMatch
data class MatchesUiState(val matches: List<SkillMatch> = emptyList(), val isLoading: Boolean = true, val error: String = "", val searchQuery: String = "", val selectedCategory: String = "All")
