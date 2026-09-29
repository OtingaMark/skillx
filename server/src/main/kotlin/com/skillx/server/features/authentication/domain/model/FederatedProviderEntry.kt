package com.skillx.server.features.authentication.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a federated identity provider entry stored in Firestore.
 * Using a data class instead of a map to avoid type inference issues with FieldValue.arrayUnion.
 */
@Serializable
data class FederatedProviderEntry(
    val provider: FederatedProvider,
    val externalId: String
)