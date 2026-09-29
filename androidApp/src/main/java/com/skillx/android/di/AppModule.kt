package com.skillx.android.di

import com.skillx.android.BuildConfig
import com.skillx.features.authentication.data.remote.AuthApi
import com.skillx.features.authentication.data.repository.AuthRepositoryImpl
import com.skillx.features.authentication.domain.repository.AuthRepository
import com.skillx.features.authentication.domain.usecase.*
import com.skillx.features.authentication.platform.GoogleCredentialLauncher
import com.skillx.features.authentication.platform.LinkedInOAuthLauncher
import com.skillx.features.authentication.presentation.login.LoginViewModel
import com.skillx.features.authentication.presentation.signup.SignUpViewModel
import com.skillx.features.lessons.data.remote.LessonApi
import com.skillx.features.lessons.data.repository.LessonRepositoryImpl
import com.skillx.features.lessons.domain.repository.LessonRepository
import com.skillx.features.lessons.domain.usecase.*
import com.skillx.features.matching.data.remote.MatchApi
import com.skillx.features.matching.data.repository.MatchRepositoryImpl
import com.skillx.features.matching.domain.repository.MatchRepository
import com.skillx.features.matching.domain.usecase.FindSkillMatchesUseCase
import com.skillx.features.matching.domain.usecase.ObserveLiveMatchesUseCase
import com.skillx.features.onboarding.data.remote.OnboardingApi
import com.skillx.features.onboarding.data.repository.OnboardingRepositoryImpl
import com.skillx.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.features.onboarding.presentation.availability.AvailabilityViewModel
import com.skillx.features.onboarding.presentation.goals.GoalsViewModel
import com.skillx.features.onboarding.presentation.learnskills.LearnSkillsViewModel
import com.skillx.features.onboarding.presentation.proficiency.ProficiencyViewModel
import com.skillx.features.onboarding.presentation.review.ReviewViewModel
import com.skillx.features.onboarding.presentation.teachskills.TeachSkillsViewModel
import com.skillx.features.payments.data.remote.PaymentApi
import com.skillx.features.payments.data.repository.PaymentRepositoryImpl
import com.skillx.features.payments.domain.repository.PaymentRepository
import com.skillx.features.payments.domain.usecase.LoadPointPackagesUseCase
import com.skillx.features.payments.domain.usecase.PurchasePointPackageUseCase
import com.skillx.features.points.data.remote.PointApi
import com.skillx.features.points.data.repository.PointRepositoryImpl
import com.skillx.features.points.domain.repository.PointRepository
import com.skillx.features.points.domain.usecase.LoadPointBalanceUseCase
import com.skillx.features.points.domain.usecase.ObservePointHistoryUseCase
import com.skillx.features.ratings.data.remote.RatingApi
import com.skillx.features.ratings.data.repository.RatingRepositoryImpl
import com.skillx.features.ratings.domain.repository.RatingRepository
import com.skillx.features.ratings.domain.usecase.LoadRatingSummaryUseCase
import com.skillx.features.ratings.domain.usecase.SubmitRatingUseCase
import com.skillx.features.reports.data.remote.ReportApi
import com.skillx.features.reports.data.repository.ReportRepositoryImpl
import com.skillx.features.reports.domain.repository.ReportRepository
import com.skillx.features.reports.domain.usecase.SubmitReportUseCase
import com.skillx.features.skills.data.remote.SkillApi
import com.skillx.features.skills.data.repository.SkillRepositoryImpl
import com.skillx.features.skills.domain.repository.SkillRepository
import com.skillx.features.skills.domain.usecase.*
import com.skillx.features.users.data.remote.UserApi
import com.skillx.features.users.data.repository.UserRepositoryImpl
import com.skillx.features.users.domain.repository.UserRepository
import com.skillx.features.users.domain.usecase.LoadCurrentUserUseCase
import com.skillx.features.users.domain.usecase.UpdateProfileUseCase
import com.skillx.features.users.presentation.home.HomeViewModel
import com.skillx.navigation.navigator.AppNavigator
import com.skillx.network.authentication.AuthTokenProvider
import com.skillx.network.authentication.SettingsAuthTokenProvider
import com.skillx.network.client.SkillXHttpClientFactory
import com.skillx.network.configuration.ApiConfiguration
import com.russhwolf.settings.Settings
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val appModule = module {
    // Scope
    single { CoroutineScope(SupervisorJob() + Dispatchers.Main) }

    // Navigation
    single { AppNavigator() }

    // Settings & Network
    single { Settings() }
    single<AuthTokenProvider> { SettingsAuthTokenProvider(get()) }
    single { ApiConfiguration(baseUrl = BuildConfig.API_BASE_URL) }
    single { SkillXHttpClientFactory(get(), get()).create() }

    // API Clients
    single { AuthApi(get()) }
    single { UserApi(get()) }
    single { SkillApi(get()) }
    single { MatchApi(get()) }
    single { LessonApi(get()) }
    single { PointApi(get()) }
    single { RatingApi(get()) }
    single { ReportApi(get()) }
    single { PaymentApi(get()) }
    single { OnboardingApi(get()) }

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<OnboardingRepository> { OnboardingRepositoryImpl(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<SkillRepository> { SkillRepositoryImpl(get()) }
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    single<LessonRepository> { LessonRepositoryImpl(get()) }
    single<PointRepository> { PointRepositoryImpl(get()) }
    single<RatingRepository> { RatingRepositoryImpl(get()) }
    single<ReportRepository> { ReportRepositoryImpl(get()) }
    single<PaymentRepository> { PaymentRepositoryImpl(get()) }

    // Use Cases — Auth
    single { RegisterUserUseCase(get()) }
    single { LoginUserUseCase(get()) }
    single { LoginWithGoogleUseCase(get()) }
    single { LoginWithLinkedInUseCase(get()) }
    single { LogoutUserUseCase(get()) }
    single { ObserveAuthSessionUseCase(get()) }

    // Social sign-in platform launchers
    single { GoogleCredentialLauncher(get(), BuildConfig.GOOGLE_OAUTH_SERVER_CLIENT_ID) }
    single {
        LinkedInOAuthLauncher(
            get(),
            BuildConfig.LINKEDIN_OAUTH_CLIENT_ID,
            BuildConfig.LINKEDIN_OAUTH_REDIRECT_URI
        )
    }

    // Use Cases — Users
    single { LoadCurrentUserUseCase(get()) }
    single { UpdateProfileUseCase(get()) }

    // Use Cases — Skills
    single { AddTeachingSkillUseCase(get()) }
    single { RemoveTeachingSkillUseCase(get()) }
    single { AddLearningSkillUseCase(get()) }
    single { RemoveLearningSkillUseCase(get()) }

    // Use Cases — Matching
    single { FindSkillMatchesUseCase(get()) }
    single { ObserveLiveMatchesUseCase(get()) }

    // Use Cases — Lessons
    single { CreateLessonRequestUseCase(get()) }
    single { AcceptLessonRequestUseCase(get()) }
    single { CompleteLessonUseCase(get()) }
    single { ObserveMyLessonsUseCase(get()) }

    // Use Cases — Points
    single { LoadPointBalanceUseCase(get()) }
    single { ObservePointHistoryUseCase(get()) }

    // Use Cases — Ratings
    single { SubmitRatingUseCase(get()) }
    single { LoadRatingSummaryUseCase(get()) }

    // Use Cases — Reports
    single { SubmitReportUseCase(get()) }

    // Use Cases — Payments
    single { LoadPointPackagesUseCase(get()) }
    single { PurchasePointPackageUseCase(get()) }

    // ViewModels
    single { SignUpViewModel(get(), get(), get(), get(), get(), get(), get()) }
    single { LoginViewModel(get(), get(), get(), get(), get(), get(), get()) }

    // ViewModels — Onboarding
    single { TeachSkillsViewModel(get(), get(), get()) }
    single { LearnSkillsViewModel(get(), get(), get()) }
    single { ProficiencyViewModel(get(), get()) }
    single { GoalsViewModel(get(), get()) }
    single { AvailabilityViewModel(get(), get()) }
    single { ReviewViewModel(get(), get()) }

    // ViewModels — Home
    single { HomeViewModel(get(), get(), get()) }
}

