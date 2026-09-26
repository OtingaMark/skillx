package com.skillx.features.lessons.presentation.list
import com.skillx.features.lessons.domain.model.LessonRequest
data class LessonRequestsUiState(val requests: List<LessonRequest> = emptyList(), val isLoading: Boolean = true, val error: String = "", val selectedTab: String = "Learning")
