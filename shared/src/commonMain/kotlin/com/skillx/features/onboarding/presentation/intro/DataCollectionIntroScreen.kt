package com.skillx.features.onboarding.presentation.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SkillIllustration
import com.skillx.designsystem.theme.SkillXColors

/**
 * Intro screen explaining the purpose of data collection.
 */
@Composable
fun DataCollectionIntroScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillXColors.Background)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            SkillIllustration()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Build Your Skill Profile",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tell us what you know and what you want to learn. This helps us find the right people and opportunities for you.",
            style = MaterialTheme.typography.bodyMedium,
            color = SkillXColors.TextSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            colors = CardDefaults.cardColors(containerColor = SkillXColors.Success.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "We'll use this information to:",
                    fontWeight = FontWeight.SemiBold,
                    color = SkillXColors.TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                listOf(
                    "Match you with the best people",
                    "Recommend relevant skills",
                    "Personalize your experience",
                    "Build your skill network"
                ).forEach { line ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = SkillXColors.Success)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = line, color = SkillXColors.TextSecondary)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You control what appears on your public profile.",
            style = MaterialTheme.typography.bodySmall,
            color = SkillXColors.TextSecondary
        )
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(text = "Continue", onClick = onContinue)
    }
}
