package com.skillx.server.di

import com.skillx.server.configuration.AppConfig
import com.skillx.server.configuration.FirebaseConfig
import com.skillx.server.configuration.JobRunrConfig
import com.skillx.server.configuration.JwtConfig
import com.skillx.server.configuration.OAuthConfig
import com.skillx.server.configuration.StripeConfig
import com.skillx.server.features.authentication.application.usecase.AuthenticateWithFederatedProviderUseCase
import com.skillx.server.features.authentication.application.usecase.LoginUserUseCase
import com.skillx.server.features.authentication.application.usecase.RegisterUserUseCase
import com.skillx.server.features.authentication.domain.repository.AuthRepository
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProvider
import com.skillx.server.features.authentication.domain.repository.FederatedIdentityProviderRegistry
import com.skillx.server.features.authentication.infrastructure.firestore.FirestoreAuthDataSource
import com.skillx.server.features.lessons.application.usecase.AcceptLessonRequestUseCase
import com.skillx.server.features.lessons.application.usecase.CompleteLessonUseCase
import com.skillx.server.features.lessons.application.usecase.CreateLessonRequestUseCase
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.infrastructure.firestore.FirestoreLessonDataSource
import com.skillx.server.features.onboarding.application.usecase.CompleteOnboardingUseCase
import com.skillx.server.features.onboarding.application.usecase.LoadOnboardingProgressUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveAvailabilityUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveGoalsUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveLearnSkillsUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveProficiencyUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveTeachSkillsUseCase
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.server.features.onboarding.infrastructure.firestore.FirestoreOnboardingDataSource
import com.skillx.server.features.points.domain.repository.PointLedgerRepository
import com.skillx.server.features.points.domain.repository.PointRepository
import com.skillx.server.features.points.domain.service.PointTransferService
import com.skillx.server.features.points.infrastructure.firestore.FirestorePointDataSource
import com.skillx.server.features.skills.application.usecase.AddLearningSkillUseCase
import com.skillx.server.features.skills.application.usecase.AddTeachingSkillUseCase
import com.skillx.server.features.skills.application.usecase.RemoveLearningSkillUseCase
import com.skillx.server.features.skills.application.usecase.RemoveTeachingSkillUseCase
import com.skillx.server.features.users.application.usecase.UpdateUserProfileUseCase
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.features.users.infrastructure.firestore.FirestoreUserDataSource
import com.skillx.server.infrastructure.authentication.JwtTokenService
import com.skillx.server.infrastructure.authentication.PasswordHasher
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner
import com.skillx.server.infrastructure.jobs.JobRunrScheduler
import com.skillx.server.infrastructure.jobs.job.ProcessLessonCompletionJob
import com.skillx.server.infrastructure.jobs.job.ReconcilePaymentJob
import com.skillx.server.infrastructure.jobs.job.RefreshMatchesJob
import com.skillx.server.infrastructure.notifications.FirebaseAdminPushSender
import com.skillx.server.infrastructure.oauth.GoogleIdentityProvider
import com.skillx.server.infrastructure.oauth.GoogleTokenExchangeClient
import com.skillx.server.infrastructure.oauth.LinkedInIdentityProvider
import com.skillx.server.infrastructure.oauth.LinkedInTokenExchangeClient
import com.skillx.server.infrastructure.oauth.OidcJwksVerifier
import com.skillx.server.infrastructure.payments.StripeClient
import com.skillx.server.infrastructure.payments.StripeWebhookVerifier
import io.ktor.client.*
import org.jobrunr.storage.StorageProvider
import org.koin.dsl.module
import javax.sql.DataSource

val serverModule = module {
    // Configuration
    single { AppConfig.fromEnvironment() }
    single { JwtConfig.fromEnvironment() }
    single { FirebaseConfig.fromEnvironment() }
    single { StripeConfig.fromEnvironment() }
    single { JobRunrConfig.fromEnvironment() }
    single { OAuthConfig.fromEnvironment() }

    // Core infrastructure
    single { JwtTokenService(get()) }
    single { PasswordHasher }
    single { FirestoreClientProvider(get()) }
    single { FirestoreTransactionRunner(get()) }
    single { OidcJwksVerifier(
        issuer = get<OAuthConfig>().google.issuer,
        audience = get<OAuthConfig>().google.clientId,
        jwksUrl = get<OAuthConfig>().google.jwksUrl
    ) }
    single { HttpClient() }
    single { OidcJwksVerifier(
        issuer = get<OAuthConfig>().linkedin.issuer,
        audience = get<OAuthConfig>().linkedin.clientId,
        jwksUrl = get<OAuthConfig>().linkedin.jwksUrl
    ) }
    single { LinkedInTokenExchangeClient(get(), get()) }
    single { GoogleTokenExchangeClient(get(), get()) }
    single { GoogleIdentityProvider(get(), get()) }
    single { LinkedInIdentityProvider(get(), get()) }
    single { FederatedIdentityProviderRegistry(listOf(get(), get())) }
    // Repositories
    single<AuthRepository> { FirestoreAuthDataSource(get()) }
    single<LessonRepository> { FirestoreLessonDataSource(get()) }
    single<PointRepository> { FirestorePointDataSource(get()) }
    single<PointLedgerRepository> { FirestorePointDataSource(get()) }
    single<UserRepository> { FirestoreUserDataSource(get()) }
    single<OnboardingRepository> { FirestoreOnboardingDataSource(get()) }

    // Use Cases
    single { RegisterUserUseCase(get(), get()) }
    single { LoginUserUseCase(get(), get()) }
    single { CreateLessonRequestUseCase(get(), get(), get()) }
    single { AcceptLessonRequestUseCase(get(), get()) }
    single { CompleteLessonUseCase(get(), get(), get()) }
    single { LoadOnboardingProgressUseCase(get()) }
    single { SaveTeachSkillsUseCase(get()) }
    single { SaveLearnSkillsUseCase(get()) }
    single { SaveProficiencyUseCase(get()) }
    single { SaveGoalsUseCase(get()) }
    single { SaveAvailabilityUseCase(get()) }
    single { UpdateUserProfileUseCase(get(), get()) }
    single { AddTeachingSkillUseCase(get(), get()) }
    single { RemoveTeachingSkillUseCase(get(), get()) }
    single { AddLearningSkillUseCase(get(), get()) }
    single { RemoveLearningSkillUseCase(get(), get()) }
    single { CompleteOnboardingUseCase(get(), get(), get(), get()) }

    // Domain Services
    single { PointTransferService(get(), get()) }
    single { AuthenticateWithFederatedProviderUseCase(get(), get(), get()) }

    // JobRunr storage provider
    single<StorageProvider> {
        org.jobrunr.storage.InMemoryStorageProvider()
    }

    // JobRunr scheduler and jobs
    single { JobRunrScheduler(get(), get(), get()) }
    single { ProcessLessonCompletionJob(get(), get()) }
    single { ReconcilePaymentJob(get(), get()) }
    single { RefreshMatchesJob(get(), get()) }

    // Notifications
    single { FirebaseAdminPushSender() }

    // Payments
    single { StripeClient(get()) }
    single { StripeWebhookVerifier(get()) }
}