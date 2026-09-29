package com.skillx.features.onboarding.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.skillx.features.onboarding.presentation.intro.DataCollectionIntroScreen
import com.skillx.features.onboarding.presentation.teachskills.TeachSkillsScreen
import com.skillx.features.onboarding.presentation.teachskills.TeachSkillsViewModel
import com.skillx.features.onboarding.presentation.learnskills.LearnSkillsScreen
import com.skillx.features.onboarding.presentation.learnskills.LearnSkillsViewModel
import com.skillx.features.onboarding.presentation.proficiency.ProficiencyScreen
import com.skillx.features.onboarding.presentation.proficiency.ProficiencyViewModel
import com.skillx.features.onboarding.presentation.goals.GoalsScreen
import com.skillx.features.onboarding.presentation.goals.GoalsViewModel
import com.skillx.features.onboarding.presentation.availability.AvailabilityScreen
import com.skillx.features.onboarding.presentation.availability.AvailabilityViewModel
import com.skillx.features.onboarding.presentation.review.ReviewScreen
import com.skillx.features.onboarding.presentation.review.ReviewViewModel
import com.skillx.features.onboarding.presentation.complete.OnboardingCompleteScreen
import org.koin.compose.koinInject

/**
 * Onboarding navigation graph.
 * Manages the wizard flow through all onboarding steps.
 */
@Composable
fun OnboardingNavGraph(onOnboardingComplete: () -> Unit) {
    var step by remember { mutableStateOf(OnboardingStep.INTRO) }

    // Every onboarding screen is a plain edge-to-edge Column with no inset handling of its
    // own — fixed once here, at the graph root, rather than in each of the 8 screens.
    Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
    when (step) {
        OnboardingStep.INTRO -> DataCollectionIntroScreen(onContinue = { step = OnboardingStep.TEACH_SKILLS })

        OnboardingStep.TEACH_SKILLS ->
            TeachSkillsScreen(
                viewModel = koinInject<TeachSkillsViewModel>(),
                onNext = { step = OnboardingStep.LEARN_SKILLS },
                onBack = { step = OnboardingStep.INTRO }
            )

        OnboardingStep.LEARN_SKILLS ->
            LearnSkillsScreen(
                viewModel = koinInject<LearnSkillsViewModel>(),
                onNext = { step = OnboardingStep.PROFICIENCY },
                onBack = { step = OnboardingStep.TEACH_SKILLS }
            )

        OnboardingStep.PROFICIENCY ->
            ProficiencyScreen(
                viewModel = koinInject<ProficiencyViewModel>(),
                onNext = { step = OnboardingStep.GOALS },
                onBack = { step = OnboardingStep.LEARN_SKILLS }
            )

        OnboardingStep.GOALS ->
            GoalsScreen(
                viewModel = koinInject<GoalsViewModel>(),
                onNext = { step = OnboardingStep.AVAILABILITY },
                onBack = { step = OnboardingStep.PROFICIENCY }
            )

        OnboardingStep.AVAILABILITY ->
            AvailabilityScreen(
                viewModel = koinInject<AvailabilityViewModel>(),
                onNext = { step = OnboardingStep.REVIEW },
                onBack = { step = OnboardingStep.GOALS }
            )

        OnboardingStep.REVIEW ->
            ReviewScreen(
                viewModel = koinInject<ReviewViewModel>(),
                onEditStep = { step = it },
                onFinish = { step = OnboardingStep.COMPLETE }
            )

        OnboardingStep.COMPLETE ->
            OnboardingCompleteScreen(onGoToHome = onOnboardingComplete)
    }
    }
}