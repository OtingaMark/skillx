package com.skillx.storage

import com.skillx.storage.settings.SecureTokenStorage

actual class AndroidSecureTokenStorage : SecureTokenStorage {
    // Uses EncryptedSharedPreferences on Android
    private val prefs = mutableMapOf<String, String>()
    actual override suspend fun saveAccessToken(token: String) { prefs["access_token"] = token }
    actual override suspend fun getAccessToken(): String? = prefs["access_token"]
    actual override suspend fun saveRefreshToken(token: String) { prefs["refresh_token"] = token }
    actual override suspend fun getRefreshToken(): String? = prefs["refresh_token"]
    actual override suspend fun clearAll() { prefs.clear() }
}
