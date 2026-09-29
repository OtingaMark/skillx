package com.skillx.server.features.authentication.domain.model

/**
 * What every provider resolves down to, once verified — the rest of the system never
 * needs to know which provider was used past this point.
 */
data class FederatedIdentity(
    val provider: FederatedProvider,
    val externalId: String,     // the OIDC `sub` claim — stable per-provider user identifier
    val email: String?,         // nullable — LinkedIn does not guarantee this
    val emailVerified: Boolean,
    val displayName: String?,
    val avatarUrl: String?
)