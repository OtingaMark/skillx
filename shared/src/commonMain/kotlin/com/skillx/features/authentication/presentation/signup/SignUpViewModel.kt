package com.skillx.features.authentication.presentation.signup

import com.skillx.core.result.AppResult
import com.skillx.features.authentication.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) { _uiState.update { it.copy(name = name) } }
    fun onEmailChanged(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChanged(password: String) { _uiState.update { it.copy(password = password) } }

    fun onSignUp() {
        val state = _uiState.value
        if (state.isLoading) return
        _uiState.update { it.copy(isLoading = true, message = "") }

        viewModelScope.launch {
            val result = registerUserUseCase(state.name, state.email, state.password)
            when (result) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false, isSuccess = true, message = "Account created successfully.") }
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, message = result.error.message) }
            }
        }
    }
}
