package com.skillx.features.onboarding.presentation.availability

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.presentation.component.OnboardingProgressIndicator

/**
 * Availability screen - Step 5 of 6
 * Day toggles, lesson format, preferred duration, languages.
 */
@Composable
fun AvailabilityScreen(
    viewModel: AvailabilityViewModel,
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
        OnboardingProgressIndicator(currentStep = 5)

        Text(
            text = "Availability & Preferences",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )
        Text(
            text = "When are you available and how do you prefer to learn/teach.",
            style = MaterialTheme.typography.bodyMedium,
            color = SkillXColors.TextSecondary
        )

        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Available days — 7 pill toggles in a row
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Availability", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = SkillXColors.TextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AvailabilityDay.values().forEach { day ->
                        val selected = day in uiState.availableDays
                        DayPill(
                            label = day.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                            selected = selected,
                            onClick = { viewModel.onDayChanged(day, !selected) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Lesson format — 4-across icon grid
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Lesson Format", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = SkillXColors.TextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        LessonFormat.VIDEO to "Video",
                        LessonFormat.AUDIO to "Audio",
                        LessonFormat.CHAT to "Chat",
                        LessonFormat.IN_PERSON to "In-person"
                    ).forEach { (format, label) ->
                        val selected = format in uiState.lessonFormats
                        FormatButton(
                            label = label,
                            icon = formatIcon(format),
                            selected = selected,
                            onClick = { viewModel.onFormatChanged(format, !selected) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Preferred duration
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Preferred Duration", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = SkillXColors.TextPrimary)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(15, 30, 60, 90).forEach { minutes ->
                        FilterChip(
                            selected = uiState.preferredDurationMinutes == minutes,
                            onClick = { viewModel.onDurationChanged(minutes) },
                            label = { Text("$minutes min") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SkillXColors.Primary,
                                selectedLabelColor = SkillXColors.OnPrimary,
                                containerColor = SkillXColors.Surface,
                                labelColor = SkillXColors.TextPrimary
                            ),
                            shape = RoundedCornerShape(percent = 50)
                        )
                    }
                }
            }

            // Languages
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Languages", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = SkillXColors.TextPrimary)
                Text(
                    "One per line, e.g. \"English - Fluent\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = SkillXColors.TextSecondary
                )
                OutlinedTextField(
                    value = uiState.languagesText,
                    onValueChange = viewModel::onLanguagesTextChanged,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 4,
                    shape = RoundedCornerShape(16.dp)
                )
                val languageEntries = uiState.languagesText.lines().map { it.trim() }.filter { it.isNotBlank() }
                if (languageEntries.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(languageEntries) { entry ->
                            LanguageChip(entry)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SkillXColors.Primary),
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
            Text(text = uiState.error, color = SkillXColors.Error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        }
    }
}

@Composable
private fun DayPill(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(44.dp)
            .background(if (selected) SkillXColors.Primary else SkillXColors.Surface, RoundedCornerShape(percent = 50))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) Color.White else SkillXColors.TextSecondary
        )
    }
}

@Composable
private fun FormatButton(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(if (selected) SkillXColors.Primary else SkillXColors.Surface, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = if (selected) Color.White else SkillXColors.TextSecondary)
        Text(label, style = MaterialTheme.typography.labelSmall, color = if (selected) Color.White else SkillXColors.TextSecondary)
    }
}

@Composable
private fun LanguageChip(text: String) {
    Row(
        modifier = Modifier
            .background(SkillXColors.PrimaryContainer, RoundedCornerShape(percent = 50))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = SkillXColors.Primary)
    }
}

private fun formatIcon(format: LessonFormat): ImageVector = when (format) {
    LessonFormat.VIDEO -> Icons.Default.Videocam
    LessonFormat.AUDIO -> Icons.Default.Mic
    LessonFormat.CHAT -> Icons.Default.Chat
    LessonFormat.IN_PERSON -> Icons.Default.LocationOn
}
