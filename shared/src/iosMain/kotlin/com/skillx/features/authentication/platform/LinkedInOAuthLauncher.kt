package com.skillx.features.authentication.platform

import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem

actual class LinkedInOAuthLauncher(
    private val clientId: String,
    private val redirectUri: String
) {
    actual suspend fun requestAuthorizationCode(): String {
        val state = PkceGenerator.generateCodeVerifier()

        val components = NSURLComponents(string = "https://www.linkedin.com/oauth/v2/authorization")
        components.queryItems = listOf(
            NSURLQueryItem(name = "response_type", value = "code"),
            NSURLQueryItem(name = "client_id", value = clientId),
            NSURLQueryItem(name = "redirect_uri", value = redirectUri),
            NSURLQueryItem(name = "scope", value = "openid profile email"),
            NSURLQueryItem(name = "state", value = state)
        )
        val authUrl = requireNotNull(components.URL) { "Failed to build LinkedIn authorization URL." }

        return runWebAuthenticationSession(authUrl, callbackScheme = "skillx", expectedState = state)
    }
}
