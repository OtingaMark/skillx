package com.skillx.features.authentication.platform

import android.net.Uri
import kotlinx.coroutines.CancellableContinuation

/**
 * Bridges the OS-level redirect back into MainActivity (registered for the
 * skillx://oauth/linkedin/callback intent-filter) to the coroutine suspended inside
 * [LinkedInOAuthLauncher], matched by the one-time `state` value each launch registers.
 */
object LinkedInOAuthCallbackRegistry {
    private val pending = mutableMapOf<String, CancellableContinuation<String>>()

    fun register(state: String, continuation: CancellableContinuation<String>) {
        pending[state] = continuation
    }

    fun unregister(state: String) {
        pending.remove(state)
    }

    fun complete(uri: Uri) {
        val state = uri.getQueryParameter("state") ?: return
        val continuation = pending.remove(state) ?: return
        val code = uri.getQueryParameter("code")
        if (code != null) {
            continuation.resumeWith(Result.success(code))
        } else {
            val error = uri.getQueryParameter("error") ?: "unknown_error"
            continuation.resumeWith(Result.failure(RuntimeException("LinkedIn sign-in failed: $error")))
        }
    }
}
