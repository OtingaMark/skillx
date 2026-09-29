package com.skillx.features.authentication.platform

import platform.Foundation.NSBundle

/**
 * Public OAuth client IDs (not secrets) — read from Info.plist, mirroring how androidApp
 * sources the same values via BuildConfig. Swap the placeholder values checked into
 * Info.plist for real ones from the Google Cloud Console / LinkedIn Developer Portal.
 *
 * Redirect URIs are fixed, non-secret conventions (must match the server's
 * GOOGLE_OAUTH_IOS_REDIRECT_URI / LINKEDIN_OAUTH_REDIRECT_URI exactly), so they're
 * plain constants here rather than another moving part in Info.plist.
 */
internal object IosOAuthClientConfig {
    const val GOOGLE_REDIRECT_URI = "skillx://oauth/google/callback"
    const val LINKEDIN_REDIRECT_URI = "skillx://oauth/linkedin/callback"

    val googleServerClientId: String get() = infoPlistString("GOOGLE_OAUTH_SERVER_CLIENT_ID")
    val linkedInClientId: String get() = infoPlistString("LINKEDIN_OAUTH_CLIENT_ID")

    private fun infoPlistString(key: String): String =
        NSBundle.mainBundle.objectForInfoDictionaryKey(key) as? String ?: ""
}
