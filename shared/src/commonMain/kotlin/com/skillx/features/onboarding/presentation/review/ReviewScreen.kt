package com.skillx.features.onboarding.presentation.review

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillx.designsystem.theme.SkillXColors
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.presentation.component.OnboardingProgressIndicator

/**
 * Review screen - Step 6 of 6
 * Shows summary of all collected data with Edit links for each section.
 */
@Composable
fun ReviewScreen(
    viewModel: ReviewViewModel,
    onEditStep: (com.skillx.features.onboarding.presentation.OnboardingStep) -> Unit,
    onFinish: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillXColors.Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OnboardingProgressIndicator(currentStep = 6)

        Text(
            text = "Review & Complete",
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )
        Text(
            text = "Here's what we've collected. You can edit any section before finishing.",
            style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
            color = SkillXColors.TextSecondary
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = SkillXColors.Primary
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.reviewItems) { item ->
                    val (icon, color) = reviewIconFor(item.title)
                    ReviewItemCard(
                        title = item.title,
                        value = item.value,
                        icon = icon,
                        iconColor = color,
                        onEdit = { onEditStep(item.step) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onFinish(onFinish) },
                colors = ButtonDefaults.buttonColors(containerColor = SkillXColors.Primary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = !uiState.saving
            ) {
                Text(text = if (uiState.saving) "Finishing…" else "Finish Setup", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        if (uiState.error.isNotBlank()) {
            Text(text = uiState.error, color = SkillXColors.Error, style = androidx.compose.material3.MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }
    }
}

/**
 * Review item card with a section icon, title/value, and an edit link.
 */
@Composable
fun ReviewItemCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SkillXColors.Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, style = MaterialTheme.typography.labelMedium, color = SkillXColors.TextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = value, style = MaterialTheme.typography.bodyMedium, color = SkillXColors.TextPrimary, maxLines = 2)
                }
            }
            TextButton(onClick = onEdit) {
                Text("Edit", color = SkillXColors.Primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun reviewIconFor(title: String): Pair<ImageVector, Color> = when (title) {
    "Teach Skills" -> Icons.Default.School to SkillXColors.Primary
    "Learn Skills" -> Icons.Default.MenuBook to SkillXColors.Secondary
    "Proficiency Levels" -> Icons.Default.Star to SkillXColors.Accent
    "Goals" -> Icons.Default.Flag to SkillXColors.Warning
    "Availability" -> Icons.Default.CalendarMonth to SkillXColors.Success
    "Languages" -> Icons.Default.Language to SkillXColors.Secondary
    else -> Icons.Default.Star to SkillXColors.Primary
}