package com.skillx.server.features.authentication.domain.model

/**
 * What the client actually hands the server — shape differs per provider by construction.
 */
sealed interface FederatedCredential {
    data class GoogleIdToken(val idToken: String) : FederatedCredential

    /**
     * iOS has no Credential Manager equivalent, so it authenticates via a browser-based
     * PKCE authorization-code flow instead — a public client, no client secret involved.
     */
    data class GoogleAuthorizationCode(
        val code: String,
        val codeVerifier: String,
        val redirectUri: String
    ) : FederatedCredential

    data class LinkedInAuthorizationCode(val code: String, val redirectUri: String) : FederatedCredential
}