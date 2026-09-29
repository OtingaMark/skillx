package com.skillx.features.onboarding.presentation.goals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.presentation.component.OnboardingProgressIndicator

/**
 * Goals screen - Step 4 of 6
 * Two side-by-side checkbox columns: learning motivation and teaching motivation.
 */
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillXColors.Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OnboardingProgressIndicator(currentStep = 4)

        Text(
            text = "Learning & Teaching Goals",
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )
        Text(
            text = "Help us understand your motivation.",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            color = SkillXColors.TextSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoalColumn(
                title = "Why do you want to learn?",
                modifier = Modifier.weight(1f)
            ) {
                LearningGoal.values().forEach { goal ->
                    CompactCheckboxRow(
                        label = goal.displayLabel(),
                        checked = goal in uiState.learningGoals,
                        onCheckedChange = { viewModel.onLearningGoalChanged(goal, it) }
                    )
                }
            }
            GoalColumn(
                title = "Why do you want to teach?",
                modifier = Modifier.weight(1f)
            ) {
                TeachingGoal.values().forEach { goal ->
                    CompactCheckboxRow(
                        label = goal.displayLabel(),
                        checked = goal in uiState.teachingGoals,
                        onCheckedChange = { viewModel.onTeachingGoalChanged(goal, it) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = SkillXColors.Primary
                ),
                border = BorderStroke(1.dp, SkillXColors.Primary.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier.weight(1f).height(52.dp)
            ) {
                Text("Back", fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = { viewModel.onNext(onNext) },
                colors = ButtonDefaults.buttonColors(containerColor = SkillXColors.Primary),
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier.weight(1f).height(52.dp),
                enabled = !uiState.saving
            ) {
                Text(text = if (uiState.saving) "Saving…" else "Next", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        if (uiState.error.isNotBlank()) {
            Text(text = uiState.error, color = SkillXColors.Error, style = androidx.compose.material3.MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }
    }
}

@Composable
private fun GoalColumn(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = SkillXColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

@Composable
private fun CompactCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = SkillXColors.Primary)
        )
        Text(
            text = label,
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
            color = SkillXColors.TextPrimary
        )
    }
}

private fun LearningGoal.displayLabel(): String = when (this) {
    LearningGoal.HOBBY -> "Hobby"
    LearningGoal.PERSONAL_DEVELOPMENT -> "Personal development"
    LearningGoal.CAREER -> "Career"
    LearningGoal.SCHOOL_UNIVERSITY -> "School / University"
    LearningGoal.CERTIFICATION -> "Certification"
    LearningGoal.BUILD_A_PROJECT -> "Build a project"
    LearningGoal.OTHER -> "Other"
}

private fun TeachingGoal.displayLabel(): String = when (this) {
    TeachingGoal.SHARE_KNOWLEDGE -> "Share knowledge"
    TeachingGoal.HELP_BEGINNERS -> "Help beginners"
    TeachingGoal.EARN_POINTS -> "Earn points"
    TeachingGoal.BUILD_REPUTATION -> "Build reputation"
    TeachingGoal.BUILD_COMMUNITY -> "Build community"
    TeachingGoal.PRACTICE_OWN_KNOWLEDGE -> "Practice my own knowledge"
}
