package com.skillx.features.authentication.platform

import com.skillx.features.authentication.domain.model.GoogleSignInCredential
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.ASWebAuthenticationSession
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem

/**
 * iOS has no Credential Manager equivalent, so Google sign-in goes through a browser
 * PKCE authorization-code flow instead (Google's own supported pattern for native apps
 * that don't embed the GIDSignIn SDK) — the server exchanges the code, no client secret
 * is ever held on-device.
 */
actual class GoogleCredentialLauncher(
    private val clientId: String,
    private val redirectUri: String
) {
    actual suspend fun requestCredential(): GoogleSignInCredential {
        val codeVerifier = PkceGenerator.generateCodeVerifier()
        val codeChallenge = PkceGenerator.codeChallenge(codeVerifier)
        val state = randomState()

        val components = NSURLComponents(string = "https://accounts.google.com/o/oauth2/v2/auth")
        components.queryItems = listOf(
            NSURLQueryItem(name = "client_id", value = clientId),
            NSURLQueryItem(name = "redirect_uri", value = redirectUri),
            NSURLQueryItem(name = "response_type", value = "code"),
            NSURLQueryItem(name = "scope", value = "openid email profile"),
            NSURLQueryItem(name = "code_challenge", value = codeChallenge),
            NSURLQueryItem(name = "code_challenge_method", value = "S256"),
            NSURLQueryItem(name = "state", value = state)
        )
        val authUrl = requireNotNull(components.URL) { "Failed to build Google authorization URL." }

        val code = runWebAuthenticationSession(authUrl, callbackScheme = "skillx", expectedState = state)
        return GoogleSignInCredential.AuthorizationCode(code, codeVerifier)
    }
}

/**
 * Runs an ASWebAuthenticationSession to completion and returns the `code` query parameter
 * from the redirect, verifying `state` to guard against a mismatched/stale callback.
 * Shared by both Google and LinkedIn's iOS launchers.
 */
internal suspend fun runWebAuthenticationSession(
    authUrl: platform.Foundation.NSURL,
    callbackScheme: String,
    expectedState: String
): String = suspendCancellableCoroutine { continuation ->
    lateinit var session: ASWebAuthenticationSession
    val presentationContextProvider = WebAuthenticationPresentationContextProvider()

    session = ASWebAuthenticationSession(
        uRL = authUrl,
        callbackURLScheme = callbackScheme
    ) { callbackUrl, error ->
        if (error != null || callbackUrl == null) {
            continuation.resumeWith(Result.failure(RuntimeException("Sign-in failed: ${error?.localizedDescription ?: "cancelled"}")))
            return@ASWebAuthenticationSession
        }

        val redirectComponents = NSURLComponents(uRL = callbackUrl, resolvingAgainstBaseURL = false)
        val queryItems = redirectComponents?.queryItems.orEmpty().filterIsInstance<NSURLQueryItem>()
        val returnedState = queryItems.firstOrNull { it.name == "state" }?.value
        val code = queryItems.firstOrNull { it.name == "code" }?.value

        when {
            returnedState != expectedState -> continuation.resumeWith(
                Result.failure(RuntimeException("Sign-in failed: state mismatch."))
            )
            code != null -> continuation.resumeWith(Result.success(code))
            else -> {
                val error = queryItems.firstOrNull { it.name == "error" }?.value ?: "unknown_error"
                continuation.resumeWith(Result.failure(RuntimeException("Sign-in failed: $error")))
            }
        }
    }

    session.presentationContextProvider = presentationContextProvider
    session.prefersEphemeralWebBrowserSession = true
    continuation.invokeOnCancellation { session.cancel() }
    session.start()
}

private fun randomState(): String = PkceGenerator.generateCodeVerifier()
