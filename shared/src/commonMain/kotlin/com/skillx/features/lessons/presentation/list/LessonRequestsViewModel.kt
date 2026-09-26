package com.skillx.features.lessons.presentation.list
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.usecase.ObserveMyLessonsUseCase
import com.skillx.features.lessons.domain.usecase.AcceptLessonRequestUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LessonRequestsViewModel(private val observeMyLessons: ObserveMyLessonsUseCase, private val acceptLesson: AcceptLessonRequestUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(LessonRequestsUiState())
    val uiState: StateFlow<LessonRequestsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() { _uiState.update { it.copy(isLoading = true) }; scope.launch { when (val r = observeMyLessons()) { is AppResult.Success -> _uiState.update { it.copy(requests = r.data, isLoading = false) }; is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = r.error.message) } } } }
    fun onTabSelected(tab: String) { _uiState.update { it.copy(selectedTab = tab) } }
    fun acceptRequest(lessonId: String) { scope.launch { acceptLesson(lessonId); load() } }
}
