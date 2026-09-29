package com.skillx.features.onboarding.presentation.proficiency

import com.skillx.core.result.AppResult
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProficiencyUiState(
    val teachSkills: List<UserSkillEntry> = emptyList(),
    val proficiencyLevels: Map<com.skillx.core.identifiers.SkillId, ProficiencyLevel> = emptyMap(),
    val isLoading: Boolean = true,
    val saving: Boolean = false,
    val error: String = ""
) {
    val allTeachSkillsHaveProficiency: Boolean
        get() = teachSkills.all { proficiencyLevels.containsKey(it.skillId) }
}

class ProficiencyViewModel(
    private val onboardingRepository: OnboardingRepository,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(ProficiencyUiState())
    val uiState: StateFlow<ProficiencyUiState> = _uiState.asStateFlow()

    init {
        loadProgress()
    }

    fun loadProgress() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = onboardingRepository.loadProgress()
            when (result) {
                is AppResult.Success -> {
                    val teachSkills = result.data.teachSkills
                    val proficiencyMap = teachSkills.associateWith { it.proficiency }.filterValues { it != null } as Map<com.skillx.core.identifiers.SkillId, com.skillx.features.onboarding.domain.model.ProficiencyLevel>
                    _uiState.update { it.copy(isLoading = false, teachSkills = teachSkills, proficiencyLevels = proficiencyMap) }
                }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, error = result.error.message) }
            }
        }
    }

    fun onProficiencyChanged(skillId: com.skillx.core.identifiers.SkillId, level: com.skillx.features.onboarding.domain.model.ProficiencyLevel) {
        _uiState.update { it.copy(proficiencyLevels = it.proficiencyLevels + (skillId to level)) }
    }

    fun onNext(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.saving || !state.allTeachSkillsHaveProficiency) return
        _uiState.update { it.copy(saving = true, error = "") }
        viewModelScope.launch {
            val entries = state.teachSkills.map { it.copy(proficiency = state.proficiencyLevels[it.skillId]) }
            val result = onboardingRepository.saveProficiency(entries)
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