package com.skillx.features.authentication.presentation.signup

/**
 * UI state for the sign-up screen.
 * Holds form input values, loading status, and result messages.
 */
data class SignUpUiState(
    /** User's full name input */
    val name: String = "",
    /** User's email input */
    val email: String = "",
    /** User's password input */
    val password: String = "",
    /** True when a registration request is in progress */
    val isLoading: Boolean = false,
    /** Error or success message to display */
    val message: String = "",
    /** True when registration succeeds (triggers navigation) */
    val isSuccess: Boolean = false,
    /** Known only once isSuccess is true — null means still fetching the profile. */
    val onboardingCompleted: Boolean? = null
)