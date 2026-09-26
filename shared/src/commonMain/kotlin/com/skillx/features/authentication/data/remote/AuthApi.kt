package com.skillx.features.authentication.data.remote

import com.skillx.features.authentication.data.remote.dto.AuthResponseDto
import com.skillx.features.authentication.data.remote.dto.LoginRequestDto
import com.skillx.features.authentication.data.remote.dto.RegisterRequestDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

/**
 * Ktor client API for authentication endpoints.
 */
class AuthApi(private val client: HttpClient) {

    suspend fun register(request: RegisterRequestDto): HttpResponse {
        return client.post("/api/v1/auth/register") {
            setBody(request)
        }
    }

    suspend fun login(request: LoginRequestDto): HttpResponse {
        return client.post("/api/v1/auth/login") {
            setBody(request)
        }
    }

    suspend fun logout(): HttpResponse {
        return client.post("/api/v1/auth/logout")
    }
}
