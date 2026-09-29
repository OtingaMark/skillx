package com.skillx.server.configuration

/**
 * OAuth configuration loaded from environment variables.
 * All secrets are loaded from environment variables.
 */
data class OAuthConfig(
    val google: GoogleOAuthSettings,
    val linkedin: LinkedInOAuthSettings
) {
    data class GoogleOAuthSettings(
        val clientId: String,
        val iosRedirectUri: String,
        val issuer: String = "https://accounts.google.com",
        val jwksUrl: String = "https://www.googleapis.com/oauth2/v3/certs"
    )
    data class LinkedInOAuthSettings(
        val clientId: String,
        val clientSecret: String,
        val redirectUri: String,
        val issuer: String = "https://www.linkedin.com",
        val jwksUrl: String = "https://www.linkedin.com/oauth/openid/jwks"
    )

    companion object {
        fun fromEnvironment(): OAuthConfig {
            return OAuthConfig(
                google = GoogleOAuthSettings(
                    clientId = System.getenv("GOOGLE_OAUTH_CLIENT_ID") ?: "",
                    iosRedirectUri = System.getenv("GOOGLE_OAUTH_IOS_REDIRECT_URI") ?: "",
                    issuer = System.getenv("GOOGLE_OAUTH_ISSUER") ?: "https://accounts.google.com",
                    jwksUrl = System.getenv("GOOGLE_OAUTH_JWKS_URL") ?: "https://www.googleapis.com/oauth2/v3/certs"
                ),
                linkedin = LinkedInOAuthSettings(
                    clientId = System.getenv("LINKEDIN_OAUTH_CLIENT_ID") ?: "",
                    clientSecret = System.getenv("LINKEDIN_OAUTH_CLIENT_SECRET") ?: "",
                    redirectUri = System.getenv("LINKEDIN_OAUTH_REDIRECT_URI") ?: "",
                    issuer = System.getenv("LINKEDIN_OAUTH_ISSUER") ?: "https://www.linkedin.com",
                    jwksUrl = System.getenv("LINKEDIN_OAUTH_JWKS_URL") ?: "https://www.linkedin.com/oauth/openid/jwks"
                )
            )
        }
    }
}