package com.skillx.features.onboarding.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillx.designsystem.theme.SkillXColors
import com.skillx.features.onboarding.domain.model.ProficiencyLevel

/**
 * Five-button grid for selecting proficiency level.
 */
@Composable
fun ProficiencySelector(
    currentLevel: ProficiencyLevel?,
    onLevelChange: (ProficiencyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ProficiencyLevel.values().forEach { level ->
                val isSelected = currentLevel == level
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Button(
                        onClick = { onLevelChange(level) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) SkillXColors.Primary else SkillXColors.Surface,
                            contentColor = if (isSelected) SkillXColors.OnPrimary else SkillXColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = if (isSelected) null else BorderStroke(1.dp, SkillXColors.Primary.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = level.displayName,
                                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                                fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
                                color = if (isSelected) SkillXColors.OnPrimary else SkillXColors.TextPrimary,
                                maxLines = 1
                            )
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = level.shortDescription,
                                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                                color = if (isSelected) SkillXColors.OnPrimary.copy(alpha = 0.8f) else SkillXColors.TextSecondary,
                                maxLines = 2,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}