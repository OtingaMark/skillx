package com.skillx.features.matching.presentation.detail
import com.skillx.features.matching.domain.model.SkillMatch
data class MatchProfileUiState(val match: SkillMatch? = null, val isLoading: Boolean = false, val error: String = "")
