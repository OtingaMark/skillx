package com.skillx.server.features.authentication.domain.repository

import com.skillx.server.features.authentication.domain.model.FederatedCredential
import com.skillx.server.features.authentication.domain.model.FederatedIdentity
import com.skillx.server.features.authentication.domain.model.FederatedProvider

interface FederatedIdentityProvider {
    val provider: FederatedProvider
    suspend fun resolveIdentity(credential: FederatedCredential): FederatedIdentity
}