package com.skillx.features.onboarding.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors

/**
 * Horizontal scrollable row of skill chips for popular skills.
 */
@Composable
fun SkillChipRow(
    skills: List<String>,
    onSkillClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(skills) { skill ->
            AssistChip(
                onClick = { onSkillClick(skill) },
                label = {
                    Text(
                        text = skill,
                        style = MaterialTheme.typography.labelMedium,
                        color = SkillXColors.Primary
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SkillXColors.PrimaryContainer
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

/**
 * Row of selected skill chips with remove buttons.
 */
@Composable
fun SelectedSkillChipRow(
    skills: List<String>,
    onRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(skills) { skill ->
            AssistChip(
                onClick = { onRemove(skill) },
                label = {
                    Text(
                        text = skill,
                        style = MaterialTheme.typography.labelMedium,
                        color = SkillXColors.OnPrimary
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove $skill",
                        tint = SkillXColors.OnPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SkillXColors.Primary
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
