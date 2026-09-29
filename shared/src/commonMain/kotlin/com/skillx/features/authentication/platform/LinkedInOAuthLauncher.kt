package com.skillx.features.authentication.platform

/**
 * Launches LinkedIn's browser-based OAuth authorization flow and returns the
 * authorization code from the redirect. The server exchanges it for an ID token.
 */
expect class LinkedInOAuthLauncher {
    suspend fun requestAuthorizationCode(): String
}
