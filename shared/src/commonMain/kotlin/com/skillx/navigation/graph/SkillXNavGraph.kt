package com.skillx.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.skillx.features.authentication.presentation.login.LoginScreen
import com.skillx.features.authentication.presentation.signup.SignUpScreen
import com.skillx.features.authentication.presentation.welcome.WelcomeScreen
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.presentation.list.LessonRequestsScreen
import com.skillx.features.lessons.presentation.list.LessonRequestsViewModel
import com.skillx.features.ratings.presentation.rate.RateLessonScreen
import com.skillx.features.ratings.presentation.rate.RateLessonViewModel
import com.skillx.features.lessons.presentation.request.RequestLessonScreen
import com.skillx.features.lessons.presentation.request.RequestLessonViewModel
import com.skillx.features.matching.presentation.matchprofile.MatchProfileScreen
import com.skillx.features.matching.presentation.matches.MatchesScreen
import com.skillx.features.onboarding.presentation.OnboardingNavGraph
import com.skillx.features.payments.presentation.RevenueCatScreen
import com.skillx.features.reports.presentation.ReportUserScreen
import com.skillx.features.safety.presentation.HowSkillXWorksScreen
import com.skillx.features.safety.presentation.SafetyScreen
import com.skillx.features.skills.presentation.SkillsScreen
import com.skillx.features.users.presentation.edit.EditProfileScreen
import com.skillx.features.users.presentation.home.HomeScreen
import com.skillx.features.users.presentation.view.ProfileScreen
import com.skillx.navigation.navigator.AppNavigator
import com.skillx.navigation.route.SkillXRoute
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * Root navigation graph composable.
 * Observes the navigator's current route and renders the matching screen.
 * Replaces the when(screen) block in SkillXApp() from MainActivity.kt L211-503.
 */
@Composable
fun SkillXNavGraph(navigator: AppNavigator) {
    val currentRoute by navigator.currentRoute.collectAsState()

    when (val route = currentRoute) {
        is SkillXRoute.Welcome -> WelcomeScreen(
            onCreateAccount = { navigator.navigateTo(SkillXRoute.SignUp) },
            onLogin = { navigator.navigateTo(SkillXRoute.Login) }
        )
        is SkillXRoute.SignUp -> SignUpScreen(
            onSignUpSuccess = { onboardingCompleted ->
                navigator.navigateAndClearStack(if (onboardingCompleted) SkillXRoute.Home else SkillXRoute.Onboarding)
            },
            onBack = { navigator.goBack() },
            onNavigateToLogin = { navigator.replaceCurrent(SkillXRoute.Login) }
        )
        is SkillXRoute.Login -> LoginScreen(
            onLoginSuccess = { onboardingCompleted ->
                navigator.navigateAndClearStack(if (onboardingCompleted) SkillXRoute.Home else SkillXRoute.Onboarding)
            },
            onBack = { navigator.goBack() },
            onNavigateToSignUp = { navigator.replaceCurrent(SkillXRoute.SignUp) }
        )
        is SkillXRoute.Onboarding -> OnboardingNavGraph(
            onOnboardingComplete = { navigator.navigateAndClearStack(SkillXRoute.Home) }
        )
        is SkillXRoute.Home -> HomeScreen(
            onNavigateToProfile = { navigator.navigateTo(SkillXRoute.Profile) },
            onNavigateToSkills = { navigator.navigateTo(SkillXRoute.Skills) },
            onNavigateToMatches = { navigator.navigateTo(SkillXRoute.Matches) },
            onNavigateToLessons = { navigator.navigateTo(SkillXRoute.LessonRequests) },
            onNavigateToSafety = { navigator.navigateTo(SkillXRoute.Safety) },
            onNavigateToHowItWorks = { navigator.navigateTo(SkillXRoute.HowItWorks) },
            onNavigateToRevenueCat = { navigator.navigateTo(SkillXRoute.RevenueCat) },
            onLogout = { navigator.navigateAndClearStack(SkillXRoute.Welcome) }
        )
        is SkillXRoute.Profile -> ProfileScreen(
            onBack = { navigator.goBack() },
            onEdit = { navigator.navigateTo(SkillXRoute.EditProfile) }
        )
        is SkillXRoute.EditProfile -> EditProfileScreen(
            onBack = { navigator.goBack() },
            onSaved = { navigator.goBack() }
        )
        is SkillXRoute.Skills -> SkillsScreen(
            onBack = { navigator.goBack() }
        )
        is SkillXRoute.Matches -> MatchesScreen(
            onBack = { navigator.goBack() },
            onViewProfile = { match -> navigator.navigateTo(SkillXRoute.MatchProfile(match)) }
        )
        is SkillXRoute.MatchProfile -> MatchProfileScreen(
            match = route.match,
            onBack = { navigator.goBack() },
            onRequestLesson = { navigator.navigateTo(SkillXRoute.RequestLesson(route.match)) },
            onReportUser = { navigator.navigateTo(SkillXRoute.ReportUser(route.match)) }
        )
        is SkillXRoute.RequestLesson -> RequestLessonScreen(
            viewModel = koinInject<RequestLessonViewModel>(),
            match = route.match,
            onBack = { navigator.goBack() },
            onRequestSent = { navigator.goBack() }
        )
        is SkillXRoute.LessonRequests -> LessonRequestsScreen(
            viewModel = koinInject<LessonRequestsViewModel>(),
            onBack = { navigator.goBack() },
            onRateLesson = { request -> navigator.navigateTo(SkillXRoute.RateLesson(request)) }
        )
        is SkillXRoute.RateLesson -> RateLessonScreen(
            viewModel = koinInject<RateLessonViewModel>(
                parameters = { parametersOf(route.request.id, route.request.teacherId, route.request.teacherName) }
            ),
            onBack = { navigator.goBack() },
            onRated = { navigator.goBack() }
        )
        is SkillXRoute.Safety -> SafetyScreen(
            onBack = { navigator.goBack() }
        )
        is SkillXRoute.HowItWorks -> HowSkillXWorksScreen(
            onBack = { navigator.goBack() }
        )
        is SkillXRoute.ReportUser -> ReportUserScreen(
            match = route.match,
            onBack = { navigator.goBack() },
            onReported = { navigator.goBack() }
        )
        is SkillXRoute.RevenueCat -> RevenueCatScreen(
            onBack = { navigator.goBack() }
        )
    }
}
