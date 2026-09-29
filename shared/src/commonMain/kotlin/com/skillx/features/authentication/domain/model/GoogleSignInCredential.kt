package com.skillx.features.authentication.domain.model

/**
 * What the platform-specific Google sign-in launcher hands back.
 * Android's Credential Manager returns an ID token directly; iOS has no equivalent
 * SDK, so it authenticates via a browser PKCE authorization-code flow instead.
 */
sealed interface GoogleSignInCredential {
    data class IdToken(val idToken: String) : GoogleSignInCredential
    data class AuthorizationCode(val code: String, val codeVerifier: String) : GoogleSignInCredential
}
