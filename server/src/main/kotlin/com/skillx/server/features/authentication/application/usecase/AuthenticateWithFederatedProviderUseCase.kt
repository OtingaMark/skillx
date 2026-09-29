package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.features.authentication.domain.model.AuthenticatedPrincipal
import com.skillx.server.features.authentication.domain.model.FederatedCredential
import com.skillx.server.features.authentication.domain.model.FederatedProvider
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProviderRegistry
import com.skillx.server.infrastructure.authentication.JwtTokenService

class AuthenticateWithFederatedProviderUseCase(
    private val registry: FederatedIdentityProviderRegistry,
    private val authRepository: AuthRepository,
    private val jwtTokenService: JwtTokenService
) {
    suspend operator fun invoke(
        provider: FederatedProvider,
        credential: FederatedCredential
    ): AuthenticatedPrincipal {
        val identity = registry.get(provider).resolveIdentity(credential)

        val (userId, email) = authRepository.findByFederatedIdentity(provider, identity.externalId)
            ?.let { it.id to it.email }
            ?: identity.email?.let { authRepository.findByEmail(it) }?.also { existing ->
                // Same person, new sign-in method — link, don't duplicate.
                authRepository.linkFederatedIdentity(existing.id, provider, identity.externalId)
            }?.let { it.id to it.email }
            ?: run {
                requireNotNull(identity.email) {
                    "Your ${provider.name.lowercase().replaceFirstChar(Char::uppercase)} account " +
                        "doesn't share an email address with SkillX. Please allow email access and try again, " +
                        "or sign up with email and password instead."
                }
                // New account — same path RegisterUserUseCase already uses, so it starts with
                // onboardingCompleted = false and is routed into onboarding like any other new user.
                authRepository.createFromFederatedIdentity(identity) to identity.email
            }

        return AuthenticatedPrincipal(
            userId,
            email,
            jwtTokenService.generateToken(userId, email),
            jwtTokenService.generateRefreshToken(userId)
        )
    }
}