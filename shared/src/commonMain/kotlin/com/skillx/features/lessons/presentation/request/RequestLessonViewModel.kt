package com.skillx.features.lessons.presentation.request
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.usecase.CreateLessonRequestUseCase
import com.skillx.features.matching.domain.model.SkillMatch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RequestLessonViewModel(private val createLessonRequest: CreateLessonRequestUseCase, private val match: SkillMatch, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(RequestLessonUiState(teacherName = match.name, skill = match.matchedSkill))
    val uiState: StateFlow<RequestLessonUiState> = _uiState.asStateFlow()
    fun onSendRequest() { _uiState.update { it.copy(isSending = true) }; scope.launch { when (val r = createLessonRequest(match.uid, match.matchedSkill)) { is AppResult.Success -> _uiState.update { it.copy(isSending = false, isSuccess = true) }; is AppResult.Error -> _uiState.update { it.copy(isSending = false, error = r.error.message) } } } }
}
