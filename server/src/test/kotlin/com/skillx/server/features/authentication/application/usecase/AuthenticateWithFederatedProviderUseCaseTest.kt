package com.skillx.server.features.authentication.application.usecase

import com.skillx.server.features.authentication.domain.model.FederatedCredential
import com.skillx.server.features.authentication.domain.model.FederatedIdentity
import com.skillx.server.features.authentication.domain.model.FederatedProvider
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProvider
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProviderRegistry
import com.skillx.server.infrastructure.authentication.JwtTokenService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthenticateWithFederatedProviderUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val jwtTokenService = mockk<JwtTokenService>()
    private val googleProvider = mockk<FederatedIdentityProvider> { every { provider } returns FederatedProvider.GOOGLE }
    private val registry = FederatedIdentityProviderRegistry(listOf(googleProvider))
    private val useCase = AuthenticateWithFederatedProviderUseCase(registry, authRepository, jwtTokenService)

    @Test
    fun `signing in twice with the same Google account resolves to the same user`() = runBlocking {
        val identity = FederatedIdentity(FederatedProvider.GOOGLE, "ext-1", "user@example.com", true, "User", null)
        coEvery { googleProvider.resolveIdentity(any()) } returns identity
        coEvery { authRepository.findByFederatedIdentity(FederatedProvider.GOOGLE, "ext-1") } returns
            AuthRepository.UserData("user-1", "user@example.com")
        every { jwtTokenService.generateToken("user-1", "user@example.com") } returns "jwt-token"
        every { jwtTokenService.generateRefreshToken("user-1") } returns "refresh-token"

        val principal = useCase(FederatedProvider.GOOGLE, FederatedCredential.GoogleIdToken("token"))

        assertEquals("user-1", principal.userId)
        coVerify(exactly = 0) { authRepository.createFromFederatedIdentity(any()) }
        coVerify(exactly = 0) { authRepository.linkFederatedIdentity(any(), any(), any()) }
    }

    @Test
    fun `an existing email-password account is linked, not duplicated`() = runBlocking {
        val identity = FederatedIdentity(FederatedProvider.GOOGLE, "ext-2", "existing@example.com", true, "User", null)
        coEvery { googleProvider.resolveIdentity(any()) } returns identity
        coEvery { authRepository.findByFederatedIdentity(FederatedProvider.GOOGLE, "ext-2") } returns null
        coEvery { authRepository.findByEmail("existing@example.com") } returns
            AuthRepository.UserCredentials("user-2", "existing@example.com", "hash")
        coEvery { authRepository.linkFederatedIdentity("user-2", FederatedProvider.GOOGLE, "ext-2") } returns Unit
        every { jwtTokenService.generateToken("user-2", "existing@example.com") } returns "jwt-token"
        every { jwtTokenService.generateRefreshToken("user-2") } returns "refresh-token"

        val principal = useCase(FederatedProvider.GOOGLE, FederatedCredential.GoogleIdToken("token"))

        assertEquals("user-2", principal.userId)
        coVerify(exactly = 1) { authRepository.linkFederatedIdentity("user-2", FederatedProvider.GOOGLE, "ext-2") }
        coVerify(exactly = 0) { authRepository.createFromFederatedIdentity(any()) }
    }

    @Test
    fun `a brand-new federated user is created via the same path as email signup`() = runBlocking {
        val identity = FederatedIdentity(FederatedProvider.GOOGLE, "ext-3", "new@example.com", true, "New User", null)
        coEvery { googleProvider.resolveIdentity(any()) } returns identity
        coEvery { authRepository.findByFederatedIdentity(FederatedProvider.GOOGLE, "ext-3") } returns null
        coEvery { authRepository.findByEmail("new@example.com") } returns null
        coEvery { authRepository.createFromFederatedIdentity(identity) } returns "user-3"
        every { jwtTokenService.generateToken("user-3", "new@example.com") } returns "jwt-token"
        every { jwtTokenService.generateRefreshToken("user-3") } returns "refresh-token"

        val principal = useCase(FederatedProvider.GOOGLE, FederatedCredential.GoogleIdToken("token"))

        assertEquals("user-3", principal.userId)
        coVerify(exactly = 1) { authRepository.createFromFederatedIdentity(identity) }
        // createFromFederatedIdentity is the exact path RegisterUserUseCase's createUser also
        // uses — onboardingCompleted = false is set there, so a new OAuth user is routed into
        // onboarding just like a new email/password user.
    }

    @Test
    fun `a federated identity with no email and no existing account is rejected`() = runBlocking {
        val identity = FederatedIdentity(FederatedProvider.LINKEDIN, "ext-4", null, false, "No Email", null)
        val linkedInProvider = mockk<FederatedIdentityProvider> { every { provider } returns FederatedProvider.LINKEDIN }
        coEvery { linkedInProvider.resolveIdentity(any()) } returns identity
        val useCaseWithLinkedIn = AuthenticateWithFederatedProviderUseCase(
            FederatedIdentityProviderRegistry(listOf(linkedInProvider)),
            authRepository,
            jwtTokenService
        )
        coEvery { authRepository.findByFederatedIdentity(FederatedProvider.LINKEDIN, "ext-4") } returns null

        assertFailsWith<IllegalArgumentException> {
            useCaseWithLinkedIn(FederatedProvider.LINKEDIN, FederatedCredential.LinkedInAuthorizationCode("code", "uri"))
        }
        Unit
    }
}
