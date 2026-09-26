package com.skillx.features.authentication.domain.model

/**
 * Credentials submitted during login or registration.
 * Pure domain type — no serialization annotations.
 */
data class AuthCredentials(
    val email: String,
    val password: String,
    val name: String = ""
)
