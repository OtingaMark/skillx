package com.skillx.network.authentication

/**
 * Provides the current authentication token for API requests.
 * Platform implementations store/retrieve tokens from secure storage.
 */
interface AuthTokenProvider {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()
}
