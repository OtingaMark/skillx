package com.skillx.storage

import com.skillx.storage.settings.SecureTokenStorage

class IosSecureTokenStorage : SecureTokenStorage {
    // Uses Keychain on iOS
    private val storage = mutableMapOf<String, String>()
    override suspend fun saveAccessToken(token: String) { storage["access_token"] = token }
    override suspend fun getAccessToken(): String? = storage["access_token"]
    override suspend fun saveRefreshToken(token: String) { storage["refresh_token"] = token }
    override suspend fun getRefreshToken(): String? = storage["refresh_token"]
    override suspend fun clearAll() { storage.clear() }
}
