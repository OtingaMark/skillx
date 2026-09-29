package com.skillx.server.infrastructure.oauth

import com.skillx.server.configuration.OAuthConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Exchanges a Google PKCE authorization code for an ID token.
 * Used by the iOS client, which has no Credential Manager equivalent.
 * Public-client PKCE flow — no client secret is sent or held here.
 */
class GoogleTokenExchangeClient(
    private val client: HttpClient,
    private val config: OAuthConfig
) {
    @Serializable
    private data class GoogleTokenResponse(
        @SerialName("access_token") val accessToken: String,
        @SerialName("id_token") val idToken: String,
        @SerialName("expires_in") val expiresIn: Long
    )

    suspend fun exchangeCodeForIdToken(code: String, codeVerifier: String, redirectUri: String): String =
        withContext(Dispatchers.IO) {
            val response: GoogleTokenResponse = client.post("https://oauth2.googleapis.com/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(Parameters.build {
                    append("grant_type", "authorization_code")
                    append("code", code)
                    append("redirect_uri", redirectUri)
                    append("client_id", config.google.clientId)
                    append("code_verifier", codeVerifier)
                })
            }.body()

            response.idToken
        }
}
