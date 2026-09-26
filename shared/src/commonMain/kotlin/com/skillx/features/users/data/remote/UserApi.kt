package com.skillx.features.users.data.remote

import com.skillx.features.users.data.remote.dto.UpdateProfileRequestDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class UserApi(private val client: HttpClient) {

    suspend fun getCurrentUser(): HttpResponse {
        return client.get("/api/v1/users/me")
    }

    suspend fun getUserById(userId: String): HttpResponse {
        return client.get("/api/v1/users/$userId")
    }

    suspend fun updateProfile(request: UpdateProfileRequestDto): HttpResponse {
        return client.put("/api/v1/users/me") {
            setBody(request)
        }
    }
}
