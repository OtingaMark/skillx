package com.skillx.features.authentication.platform

/** Social sign-in is a mobile-only feature; the desktop target has no launch surface for it. */
actual class LinkedInOAuthLauncher {
    actual suspend fun requestAuthorizationCode(): String {
        throw UnsupportedOperationException("LinkedIn sign-in is not supported on desktop.")
    }
}
