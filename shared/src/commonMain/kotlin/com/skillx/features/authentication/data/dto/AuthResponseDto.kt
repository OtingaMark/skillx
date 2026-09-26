package com.skillx.features.authentication.data.dto
import kotlinx.serialization.Serializable
@Serializable data class AuthResponseDto(val userId: String, val email: String, val token: String, val refreshToken: String)
