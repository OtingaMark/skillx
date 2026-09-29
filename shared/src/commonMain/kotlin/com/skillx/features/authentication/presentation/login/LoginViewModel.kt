package com.skillx.features.authentication.presentation.login

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.authentication.domain.model.AuthSession
import com.skillx.features.authentication.domain.usecase.LoginUserUseCase
import com.skillx.features.authentication.domain.usecase.LoginWithGoogleUseCase
import com.skillx.features.authentication.domain.usecase.LoginWithLinkedInUseCase
import com.skillx.features.authentication.platform.GoogleCredentialLauncher
import com.skillx.features.authentication.platform.LinkedInOAuthLauncher
import com.skillx.features.users.domain.usecase.LoadCurrentUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginWithLinkedInUseCase: LoginWithLinkedInUseCase,
    private val googleCredentialLauncher: GoogleCredentialLauncher,
    private val linkedInOAuthLauncher: LinkedInOAuthLauncher,
    private val loadCurrentUserUseCase: LoadCurrentUserUseCase,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) { _uiState.update { it.copy(email = email) } }
    fun onPasswordChanged(password: String) { _uiState.update { it.copy(password = password) } }

    fun onLogin() {
        val state = _uiState.value
        if (state.isLoading) return
        _uiState.update { it.copy(isLoading = true, message = "") }

        viewModelScope.launch {
            when (val result = loginUserUseCase(state.email, state.password)) {
                is AppResult.Success -> onAuthSuccess()
                is AppResult.Error -> _uiState.update { it.copy(isLoading = false, message = result.error.message) }
            }
        }
    }

    fun onGoogleLoginClicked() = launchSocialLogin("Google sign-in failed.") {
        loginWithGoogleUseCase(googleCredentialLauncher.requestCredential())
    }

    fun onLinkedInLoginClicked() = launchSocialLogin("LinkedIn sign-in failed.") {
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

    // The token is valid at this point, but the screen needs to know whether to route to
    // Onboarding or Home — that's on the user's profile, not the auth session, so fetch it
    // before declaring success. A profile-load failure here defaults to onboarding (the safe
    // side — a user who hasn't onboarded is one the matching engine has nothing to work with).
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
