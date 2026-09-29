package com.skillx.features.authentication.presentation.login

/**
 * UI state for the login screen.
 * Holds form input values, loading status, and result messages.
 */
data class LoginUiState(
    /** User's email input */
    val email: String = "",
    /** User's password input */
    val password: String = "",
    /** True when an authentication request is in progress */
    val isLoading: Boolean = false,
    /** Error or success message to display */
    val message: String = "",
    /** True when login succeeds (triggers navigation) */
    val isSuccess: Boolean = false,
    /** Known only once isSuccess is true — null means still fetching the profile. */
    val onboardingCompleted: Boolean? = null
)