package com.skillx.features.users.presentation.edit
data class EditProfileUiState(val name: String = "", val teachSkills: List<String> = emptyList(), val learnSkills: List<String> = emptyList(), val isLoading: Boolean = false, val isSaving: Boolean = false, val error: String = "", val isSaved: Boolean = false)
