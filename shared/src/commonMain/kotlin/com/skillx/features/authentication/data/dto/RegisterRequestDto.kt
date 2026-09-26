package com.skillx.features.authentication.data.dto
import kotlinx.serialization.Serializable
@Serializable data class RegisterRequestDto(val name: String, val email: String, val password: String)
