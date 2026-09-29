package com.skillx.network.authentication

import com.russhwolf.settings.Settings

/**
 * Android token storage using Multiplatform-Settings.
 * In production, replace with EncryptedSharedPreferences via a Settings factory.
 */
class SettingsAuthTokenProvider(
    private val settings: Settings
) : AuthTokenProvider {
    private companion object {
        const val KEY_ACCESS_TOKEN = "skillx_access_token"
        const val KEY_REFRESH_TOKEN = "skillx_refresh_token"
    }

    override suspend fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)
    override suspend fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)
    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        settings.putString(KEY_ACCESS_TOKEN, accessToken)
        settings.putString(KEY_REFRESH_TOKEN, refreshToken)
    }
    override suspend fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }
}
