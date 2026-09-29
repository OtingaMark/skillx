package com.skillx.features.authentication.data.remote

import com.skillx.features.authentication.data.dto.GoogleSignInRequestDto
import com.skillx.features.authentication.data.dto.LinkedInSignInRequestDto
import com.skillx.features.authentication.data.dto.AuthResponseDto
import com.skillx.features.authentication.data.dto.LoginRequestDto
import com.skillx.features.authentication.data.dto.RegisterRequestDto
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

    suspend fun loginWithGoogle(request: GoogleSignInRequestDto): HttpResponse {
        return client.post("/api/v1/auth/google") {
            setBody(request)
        }
    }

    suspend fun loginWithLinkedIn(request: LinkedInSignInRequestDto): HttpResponse {
        return client.post("/api/v1/auth/linkedin") {
            setBody(request)
        }
    }

    suspend fun logout(): HttpResponse {
        return client.post("/api/v1/auth/logout")
    }
}
