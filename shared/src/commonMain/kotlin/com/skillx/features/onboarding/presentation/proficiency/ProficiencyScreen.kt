package com.skillx.features.onboarding.presentation.proficiency

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillx.designsystem.theme.SkillXColors
import com.skillx.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import com.skillx.designsystem.components.SkillAvatar
import com.skillx.features.onboarding.presentation.component.OnboardingProgressIndicator
import com.skillx.features.onboarding.presentation.component.ProficiencySelector

/**
 * Proficiency screen - Step 3 of 6
 * Shows proficiency selector for each teach skill selected earlier.
 */
@Composable
fun ProficiencyScreen(
    viewModel: ProficiencyViewModel,
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
        OnboardingProgressIndicator(currentStep = 3)

        Text(
            text = "Your Proficiency Level",
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )
        Text(
            text = "Tell us your current level for each skill.",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            color = SkillXColors.TextSecondary
        )

        if (uiState.teachSkills.isEmpty()) {
            Text(
                text = "No teach skills selected. Go back and add at least one.",
                color = SkillXColors.Error,
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.teachSkills) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SkillXColors.Surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SkillAvatar(name = entry.skillId.value)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = entry.skillId.value,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SkillXColors.TextPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            ProficiencySelector(
                                currentLevel = uiState.proficiencyLevels[entry.skillId],
                                onLevelChange = { level -> viewModel.onProficiencyChanged(entry.skillId, level) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

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
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f).height(52.dp)
            ) {
                Text("Back", fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = { viewModel.onNext(onNext) },
                colors = ButtonDefaults.buttonColors(containerColor = SkillXColors.Primary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f).height(52.dp),
                enabled = !uiState.saving && uiState.allTeachSkillsHaveProficiency
            ) {
                Text(text = if (uiState.saving) "Saving…" else "Next", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        if (uiState.error.isNotBlank()) {
            Text(text = uiState.error, color = SkillXColors.Error, style = androidx.compose.material3.MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }
    }
}