package com.skillx.features.authentication.presentation.signup

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.usecase.LoginWithGoogleUseCase
import com.skillx.features.authentication.domain.usecase.LoginWithLinkedInUseCase
import com.skillx.features.authentication.domain.usecase.RegisterUserUseCase
import com.skillx.features.authentication.platform.GoogleCredentialLauncher
import com.skillx.features.authentication.platform.LinkedInOAuthLauncher
import com.skillx.features.users.domain.usecase.LoadCurrentUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithLinkedInUseCase: LoginWithLinkedInUseCase,
    private val googleCredentialLauncher: GoogleCredentialLauncher,
    private val linkedInOAuthLauncher: LinkedInOAuthLauncher,
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
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
            when (val result = registerUserUseCase(state.name, state.email, state.password)) {
                is AppResult.Success -> onAuthSuccess()
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, message = result.error.message) }
            }
        }
    }

    // Sign-up and sign-in call the exact same two server endpoints — the server decides
    // find-vs-create, so there's no separate "sign up with Google" code path here.
    fun onGoogleSignUpClicked() = launchSocialLogin("Google sign-in failed.") {
        loginWithGoogleUseCase(googleCredentialLauncher.requestCredential())
    }

    fun onLinkedInSignUpClicked() = launchSocialLogin("LinkedIn sign-in failed.") {
        loginWithLinkedInUseCase(linkedInOAuthLauncher.requestAuthorizationCode())
    }

    private fun launchSocialLogin(failureMessage: String, action: suspend () -> AppResult<AuthSession, AppError>) {
        val state = _uiState.value
        if (state.isLoading) return
        _uiState.update { it.copy(isLoading = true, message = "") }

        viewModelScope.launch {
            val result = try {
                action()
            } catch (e: Exception) {
                AppResult.Error(NetworkError.Unknown(e.message ?: failureMessage))
            }
            when (result) {
                is AppResult.Success -> onAuthSuccess()
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, message = result.error.message) }
            }
        }
    }

    // A brand-new email/password account is never onboarded, but a "sign up" via Google/LinkedIn
    // can resolve to an existing, already-onboarded account (the server links matching emails
    // instead of duplicating) — so this always checks the real profile rather than assuming.
    private suspend fun onAuthSuccess() {
        when (val profile = loadCurrentUserUseCase()) {
            is AppResult.Success -> _uiState.update {
                it.copy(isLoading = false, isSuccess = true, onboardingCompleted = profile.data.onboardingCompleted)
            }
            is AppResult.Error -> _uiState.update {
                it.copy(isLoading = false, isSuccess = true, onboardingCompleted = false)
            }
        }
    }
}
