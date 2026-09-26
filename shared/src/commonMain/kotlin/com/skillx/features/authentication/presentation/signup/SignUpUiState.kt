package com.skillx.features.authentication.presentation.signup

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val message: String = "",
    val isSuccess: Boolean = false
)
