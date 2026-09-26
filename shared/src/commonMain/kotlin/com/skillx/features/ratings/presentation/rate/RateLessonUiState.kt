package com.skillx.features.ratings.presentation.rate
data class RateLessonUiState(val lessonId: String = "", val teacherId: String = "", val teacherName: String = "", val selectedRating: Int = 0, val comment: String = "", val isSubmitting: Boolean = false, val isSuccess: Boolean = false, val error: String = "")
