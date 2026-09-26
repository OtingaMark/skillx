package com.skillx.features.skills.presentation.skills
data class SkillsUiState(val teachSkills: List<String> = emptyList(), val learnSkills: List<String> = emptyList(), val newTeachSkill: String = "", val newLearnSkill: String = "", val isLoading: Boolean = true, val isSaving: Boolean = false, val error: String = "")
