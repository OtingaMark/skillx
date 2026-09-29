package com.skillx.features.users.presentation.home

import com.skillx.features.users.domain.model.UserProfile

/**
 * UI state for the home screen.
 * Contains the current user's profile, point balance, and any featured skills.
 */
data class HomeUiState(
    /** The currently logged-in user's profile, null if not loaded */
    val user: UserProfile? = null,
    /** True while user data is being fetched */
    val isLoading: Boolean = true,
    /** Error message if loading failed */
    val error: String = "",
    /** Current point balance (mirrored from user.points for quick access) */
    val pointBalance: Int = 0,
    /** Featured/popular skills to display on home screen */
    val featuredSkills: List<String> = emptyList()
)