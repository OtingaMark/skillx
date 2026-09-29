package com.skillx.server.features.authentication.domain.repository

import com.skillx.server.core.exceptions.AuthenticationException
import com.skillx.server.features.authentication.domain.model.FederatedProvider

class FederatedIdentityProviderRegistry(providers: List<FederatedIdentityProvider>) {
    private val byProvider = providers.associateBy { it.provider }
    
    fun get(provider: FederatedProvider): FederatedIdentityProvider =
        byProvider[provider] ?: throw AuthenticationException("Unsupported sign-in provider: $provider")
}