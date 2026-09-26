package com.skillx.features.skills.presentation.skills
import com.skillx.core.result.AppResult
import com.skillx.features.skills.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SkillsViewModel(private val addTeach: AddTeachingSkillUseCase, private val removeTeach: RemoveTeachingSkillUseCase, private val addLearn: AddLearningSkillUseCase, private val removeLearn: RemoveLearningSkillUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(SkillsUiState())
    val uiState: StateFlow<SkillsUiState> = _uiState.asStateFlow()
    fun onNewTeachSkillChanged(v: String) { _uiState.update { it.copy(newTeachSkill = v) } }
    fun onNewLearnSkillChanged(v: String) { _uiState.update { it.copy(newLearnSkill = v) } }
    fun addTeachingSkill() { val s = _uiState.value.newTeachSkill.trim(); if (s.isBlank()) return; scope.launch { when (val r = addTeach(s)) { is AppResult.Success -> _uiState.update { it.copy(teachSkills = r.data, newTeachSkill = "") }; is AppResult.Error -> _uiState.update { it.copy(error = r.error.message) } } } }
    fun removeTeachingSkill(skill: String) { scope.launch { when (val r = removeTeach(skill)) { is AppResult.Success -> _uiState.update { it.copy(teachSkills = r.data) }; is AppResult.Error -> {} } } }
    fun addLearningSkill() { val s = _uiState.value.newLearnSkill.trim(); if (s.isBlank()) return; scope.launch { when (val r = addLearn(s)) { is AppResult.Success -> _uiState.update { it.copy(learnSkills = r.data, newLearnSkill = "") }; is AppResult.Error -> _uiState.update { it.copy(error = r.error.message) } } } }
    fun removeLearningSkill(skill: String) { scope.launch { when (val r = removeLearn(skill)) { is AppResult.Success -> _uiState.update { it.copy(learnSkills = r.data) }; is AppResult.Error -> {} } } }
}
