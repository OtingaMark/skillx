package com.skillx.features.authentication.presentation.signup

sealed interface SignUpEvent {
    data class NameChanged(val name: String) : SignUpEvent
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    data object SignUpClicked : SignUpEvent
    data object GoogleSignUpClicked : SignUpEvent
    data object LinkedInSignUpClicked : SignUpEvent
}
