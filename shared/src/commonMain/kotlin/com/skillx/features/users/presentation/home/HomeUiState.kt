package com.skillx.features.users.presentation.home
import com.skillx.features.users.domain.model.UserProfile
data class HomeUiState(val user: UserProfile? = null, val isLoading: Boolean = true, val error: String = "", val pointBalance: Int = 0, val featuredSkills: List<String> = emptyList())
