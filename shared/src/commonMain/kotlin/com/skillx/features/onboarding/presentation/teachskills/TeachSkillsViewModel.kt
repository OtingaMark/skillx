package com.skillx.features.onboarding.presentation.teachskills

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.features.skills.domain.repository.SkillRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the Teach Skills screen.
 */
data class TeachSkillsUiState(
    val query: String = "",
    val searchResults: List<com.skillx.features.skills.domain.model.Skill> = emptyList(),
    val selected: List<UserSkillEntry> = emptyList(),
    val isLoading: Boolean = false,
    val saving: Boolean = false,
    val error: String = ""
)

/**
 * ViewModel for the Teach Skills screen.
 */
class TeachSkillsViewModel(
    private val skillRepository: com.skillx.features.skills.domain.repository.SkillRepository,
    private val onboardingRepository: com.skillx.features.onboarding.domain.repository.OnboardingRepository,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(TeachSkillsUiState())
    val uiState: StateFlow<TeachSkillsUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(query = query) }
        if (query.length >= 2) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                val results = skillRepository.search(query)
                _uiState.update { it.copy(isLoading = false, searchResults = results) }
            }
        } else {
            _uiState.update { it.copy(isLoading = false, searchResults = emptyList()) }
        }
    }

    fun onSkillToggled(skill: com.skillx.features.skills.domain.model.Skill) {
        _uiState.update { current ->
            val alreadySelected = current.selected.any { it.skillId == skill.id }
            current.copy(
                selected = if (alreadySelected) {
                    current.selected.filterNot { it.skillId == skill.id }
                } else {
                    current.selected + com.skillx.features.onboarding.domain.model.UserSkillEntry(
                        skillId = skill.id,
                        relation = com.skillx.features.onboarding.domain.model.SkillRelation.TEACH
                    )
                }
            )
        }
    }

    fun onNext(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.saving) return
        _uiState.update { it.copy(saving = true, error = "") }
        viewModelScope.launch {
            val result = onboardingRepository.saveTeachSkills(state.selected)
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