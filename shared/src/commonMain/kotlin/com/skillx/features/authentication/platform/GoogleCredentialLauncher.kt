package com.skillx.features.authentication.platform

import com.skillx.features.authentication.domain.model.GoogleSignInCredential

/**
 * Launches the platform's Google sign-in flow and returns a verifiable credential.
 * Android uses Credential Manager (returns an ID token directly); iOS has no
 * equivalent SDK and uses a browser-based PKCE authorization-code flow instead.
 */
expect class GoogleCredentialLauncher {
    suspend fun requestCredential(): GoogleSignInCredential
}
