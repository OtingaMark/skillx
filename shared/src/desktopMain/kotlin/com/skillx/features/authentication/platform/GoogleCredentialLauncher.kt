package com.skillx.features.authentication.platform

import com.skillx.features.authentication.domain.model.GoogleSignInCredential

/** Social sign-in is a mobile-only feature; the desktop target has no launch surface for it. */
actual class GoogleCredentialLauncher {
    actual suspend fun requestCredential(): GoogleSignInCredential {
        throw UnsupportedOperationException("Google sign-in is not supported on desktop.")
    }
}
