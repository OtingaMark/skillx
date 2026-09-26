package com.skillx.server.features.authentication.interfaces.http.request
import kotlinx.serialization.Serializable
@Serializable data class LoginRequest(val email: String, val password: String)
