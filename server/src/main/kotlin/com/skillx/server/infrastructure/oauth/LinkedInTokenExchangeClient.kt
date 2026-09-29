package com.skillx.server.infrastructure.oauth

import com.skillx.server.configuration.OAuthConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LinkedInTokenExchangeClient(
    private val client: HttpClient,
    private val config: OAuthConfig
) {
    @Serializable
    private data class LinkedInTokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("id_token") val idToken: String,
        @SerialName("expires_in") val expiresIn: Long
    )

    suspend fun exchangeCodeForIdToken(code: String, redirectUri: String): String = withContext(Dispatchers.IO) {
        val response: LinkedInTokenResponse = client.post("https://www.linkedin.com/oauth/v2/accessToken") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(io.ktor.http.Parameters.build {
                append("grant_type", "authorization_code")
                append("code", code)
                append("redirect_uri", redirectUri)
                append("client_id", config.linkedin.clientId)
                append("client_secret", config.linkedin.clientSecret)
            })
        }.body()

        return@withContext response.idToken
    }
}