package com.skillx.features.onboarding.presentation.teachskills

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.skillx.features.onboarding.domain.model.SkillRelation
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import com.skillx.designsystem.components.SkillAvatar
import com.skillx.features.onboarding.presentation.component.OnboardingProgressIndicator
import com.skillx.features.onboarding.presentation.component.SkillChipRow
import com.skillx.features.onboarding.presentation.component.SkillSearchField
import com.skillx.features.skills.domain.model.Skill

/**
 * Teach Skills screen - Step 1 of 6
 */
@Composable
fun TeachSkillsScreen(
    viewModel: TeachSkillsViewModel,
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
        OnboardingProgressIndicator(currentStep = 1)

        Text(
            text = "What skills can you teach?",
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )
        Text(
            text = "Select the skills you can teach others.",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            color = SkillXColors.TextSecondary
        )

        SkillSearchField(
            query = uiState.query,
            onQueryChange = viewModel::onSearchQueryChanged,
            hint = "Search skills…"
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(16.dp))

        // Popular skills
        Text(
            text = "Popular",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = SkillXColors.TextPrimary
        )
        SkillChipRow(
            skills = listOf("Programming", "Languages", "Music", "Sports", "Design", "Business"),
            onSkillClick = { name -> viewModel.onSkillToggled(Skill(name)) }
        )

        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(24.dp))

        // Selected skills
        if (uiState.selected.isNotEmpty()) {
            Text(
                text = "Your Skills",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = SkillXColors.TextPrimary
            )
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.selected) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SkillXColors.Surface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SkillAvatar(name = entry.skillId.value)
                            Text(
                                text = entry.skillId.value,
                                style = MaterialTheme.typography.bodyLarge,
                                color = SkillXColors.TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "Edit",
                                color = SkillXColors.Primary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { viewModel.onSkillToggled(Skill(entry.skillId.value)) }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.onNext(onNext) },
            colors = ButtonDefaults.buttonColors(containerColor = SkillXColors.Primary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !uiState.saving
        ) {
            Text(text = if (uiState.saving) "Saving…" else "Next", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        if (uiState.error.isNotBlank()) {
            Text(text = uiState.error, color = SkillXColors.Error, style = androidx.compose.material3.MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }
    }
}