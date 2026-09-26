package com.skillx.storage.settings

/**
 * expect/actual for secure token storage. Multiplatform-Settings backed.
 * Android → EncryptedSharedPreferences, iOS → Keychain.
 */
expect class SecureTokenStorage {
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearAll()
}
