package com.skillx.features.onboarding.presentation.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillx.designsystem.theme.SkillXColors

/**
 * Onboarding Complete screen
 * Celebration screen shown after successful onboarding completion.
 */
@Composable
fun OnboardingCompleteScreen(onGoToHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SkillXColors.Background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Checkmark illustration
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(SkillXColors.Success.copy(alpha = 0.15f), RoundedCornerShape(60.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = SkillXColors.Success
            )
        }

        Text(
            text = "You're All Set!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = SkillXColors.TextPrimary
        )

        Text(
            text = "You can now start exploring, finding matches and exchanging skills with the community.",
            style = MaterialTheme.typography.bodyLarge,
            color = SkillXColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onGoToHome,
            colors = ButtonDefaults.buttonColors(containerColor = SkillXColors.Primary),
            shape = RoundedCornerShape(percent = 50),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(text = "Go to Home", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        // No profile route is reachable from here without touching navigation/graph
        // plumbing, which is outside this pass's scope — shown inert rather than wired
        // to the wrong destination or a new callback nobody asked for.
        Text(
            text = "View Your Profile",
            style = MaterialTheme.typography.labelLarge,
            color = SkillXColors.TextSecondary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}