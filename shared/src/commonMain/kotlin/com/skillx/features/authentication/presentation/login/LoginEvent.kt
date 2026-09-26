package com.skillx.features.authentication.presentation.login

sealed interface LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object LoginClicked : LoginEvent
    data object GoogleLoginClicked : LoginEvent
    data object LinkedInLoginClicked : LoginEvent
    data object ForgotPasswordClicked : LoginEvent
}
