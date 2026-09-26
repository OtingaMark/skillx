package com.skillx.features.users.presentation.view
import com.skillx.features.users.domain.model.UserProfile
data class ProfileUiState(val user: UserProfile? = null, val isLoading: Boolean = true, val error: String = "", val averageRating: Double? = null, val totalRatings: Int = 0)
