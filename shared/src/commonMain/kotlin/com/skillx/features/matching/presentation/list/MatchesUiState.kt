package com.skillx.features.matching.presentation.list

import com.skillx.features.matching.domain.model.SkillMatch

/**
 * UI state for the matches list screen.
 * Holds the list of skill matches, loading state, errors, and filter options.
 */
data class MatchesUiState(
    /** Skill matches where the current user can learn from the matched user */
    val matches: List<SkillMatch> = emptyList(),
    /** True while matches are being fetched */
    val isLoading: Boolean = true,
    /** Error message if loading failed */
    val error: String = "",
    /** Search query for filtering matches by name/skill */
    val searchQuery: String = "",
    /** Selected skill category filter */
    val selectedCategory: String = "All"
)