package com.skillx.features.authentication.data.dto
import kotlinx.serialization.Serializable

/**
 * idToken is set by Android (Credential Manager); code/codeVerifier by iOS (PKCE
 * authorization-code flow, since there's no Credential Manager equivalent there).
 * The redirect URI is fixed server-side, not client-supplied.
 */
@Serializable data class GoogleSignInRequestDto(
    val idToken: String? = null,
    val code: String? = null,
    val codeVerifier: String? = null
)
