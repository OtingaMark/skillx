package com.skillx.features.users.presentation.edit
import com.skillx.core.result.AppResult
import com.skillx.features.users.domain.usecase.LoadCurrentUserUseCase
import com.skillx.features.users.domain.usecase.UpdateProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditProfileViewModel(private val loadCurrentUserUseCase: LoadCurrentUserUseCase, private val updateProfileUseCase: UpdateProfileUseCase, private val scope: CoroutineScope) {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()
    init { load() }
    private fun load() { scope.launch { when (val r = loadCurrentUserUseCase()) { is AppResult.Success -> _uiState.update { it.copy(name = r.data.name, teachSkills = r.data.teachSkills, learnSkills = r.data.learnSkills, isLoading = false) }; is AppResult.Error -> _uiState.update { it.copy(error = r.error.message, isLoading = false) } } } }
    fun onNameChanged(name: String) { _uiState.update { it.copy(name = name) } }
    fun onSave() { _uiState.update { it.copy(isSaving = true) }; scope.launch { val s = _uiState.value; when (val r = updateProfileUseCase(s.name, s.teachSkills, s.learnSkills)) { is AppResult.Success -> _uiState.update { it.copy(isSaving = false, isSaved = true) }; is AppResult.Error -> _uiState.update { it.copy(isSaving = false, error = r.error.message) } } } }
}
