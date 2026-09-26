package com.skillx.server.features.authentication.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class AuthResponse(val userId: String, val email: String, val token: String, val refreshToken: String)
