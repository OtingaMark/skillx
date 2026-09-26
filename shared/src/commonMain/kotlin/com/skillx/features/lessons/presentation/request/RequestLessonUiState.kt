package com.skillx.features.lessons.presentation.request
data class RequestLessonUiState(val teacherName: String = "", val skill: String = "", val isSending: Boolean = false, val isSuccess: Boolean = false, val error: String = "")
