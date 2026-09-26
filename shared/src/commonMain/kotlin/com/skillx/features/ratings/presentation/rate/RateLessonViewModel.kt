package com.skillx.features.ratings.presentation.rate
import com.skillx.core.result.AppResult
import com.skillx.features.ratings.domain.usecase.SubmitRatingUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RateLessonViewModel(private val submitRating: SubmitRatingUseCase, lessonId: String, teacherId: String, teacherName: String, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(RateLessonUiState(lessonId = lessonId, teacherId = teacherId, teacherName = teacherName))
    val uiState: StateFlow<RateLessonUiState> = _uiState.asStateFlow()
    fun onRatingChanged(rating: Int) { _uiState.update { it.copy(selectedRating = rating) } }
    fun onCommentChanged(comment: String) { _uiState.update { it.copy(comment = comment) } }
    fun onSubmit() { val s = _uiState.value; if (s.selectedRating == 0) { _uiState.update { it.copy(error = "Please select a rating.") }; return }
        _uiState.update { it.copy(isSubmitting = true) }; scope.launch { when (val r = submitRating(s.lessonId, s.teacherId, s.selectedRating, s.comment)) { is AppResult.Success -> _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }; is AppResult.Error -> _uiState.update { it.copy(isSubmitting = false, error = r.error.message) } } } }
}
