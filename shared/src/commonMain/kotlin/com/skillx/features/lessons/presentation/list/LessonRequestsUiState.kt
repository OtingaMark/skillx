package com.skillx.features.lessons.presentation.list

import com.skillx.features.lessons.domain.model.LessonRequest

/**
 * UI state for the lesson requests screen.
 * Holds the list of lesson requests, loading state, errors, and active tab.
 */
data class LessonRequestsUiState(
    /** All lesson requests involving the current user (as requester or teacher) */
    val requests: List<LessonRequest> = emptyList(),
    /** True while requests are being fetched */
    val isLoading: Boolean = true,
    /** Error message if loading failed */
    val error: String = "",
    /** Currently selected tab: "Learning" (requests made) or "Teaching" (requests received) */
    val selectedTab: String = "Learning"
)