package com.skillx.features.onboarding.presentation.goals

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GoalsUiState(
    val learningGoals: Set<LearningGoal> = emptySet(),
    val teachingGoals: Set<TeachingGoal> = emptySet(),
    val isLoading: Boolean = true,
    val saving: Boolean = false,
    val error: String = ""
)

class GoalsViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = onboardingRepository.loadProgress()
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(isLoading = false, learningGoals = result.data.learningGoals, teachingGoals = result.data.teachingGoals) }
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.error.message) }
            }
        }
    }

    fun onLearningGoalChanged(goal: LearningGoal, checked: Boolean) {
        _uiState.update { current ->
            val newGoals = if (checked) current.learningGoals + goal else current.learningGoals - goal
            current.copy(learningGoals = newGoals)
        }
    }

    fun onTeachingGoalChanged(goal: TeachingGoal, checked: Boolean) {
        _uiState.update { current ->
            val newGoals = if (checked) current.teachingGoals + goal else current.teachingGoals - goal
            current.copy(teachingGoals = newGoals)
        }
    }

    fun onNext(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.saving) return
        _uiState.update { it.copy(saving = true, error = "") }
        viewModelScope.launch {
            val result = onboardingRepository.saveGoals(state.learningGoals, state.teachingGoals)
            when (result) {
                is AppResult.Success -> {
                    _uiState.update { it.copy(saving = false) }
                    onSuccess()
                }
                is AppResult.Error -> _uiState.update { it.copy(saving = false, error = result.error.message) }
            }
        }
    }
}