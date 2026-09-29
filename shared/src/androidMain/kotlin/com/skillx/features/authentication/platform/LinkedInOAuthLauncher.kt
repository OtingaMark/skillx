package com.skillx.features.authentication.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID

actual class LinkedInOAuthLauncher(
    private val context: Context,
    private val clientId: String,
    private val redirectUri: String
) {
    actual suspend fun requestAuthorizationCode(): String = suspendCancellableCoroutine { continuation ->
        val state = UUID.randomUUID().toString()
        LinkedInOAuthCallbackRegistry.register(state, continuation)
        continuation.invokeOnCancellation { LinkedInOAuthCallbackRegistry.unregister(state) }

        val authUrl = Uri.parse("https://www.linkedin.com/oauth/v2/authorization").buildUpon()
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", redirectUri)
            .appendQueryParameter("scope", "openid profile email")
            .appendQueryParameter("state", state)
            .build()

        // Launched from an application Context, not an Activity, so the new task flag is required.
        CustomTabsIntent.Builder().build().apply {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.launchUrl(context, authUrl)
    }
}
