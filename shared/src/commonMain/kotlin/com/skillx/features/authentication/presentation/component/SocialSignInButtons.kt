package com.skillx.features.authentication.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors

/**
 * Shared by LoginScreen and SignUpScreen — both call the same two server endpoints,
 * since the server decides find-vs-create either way.
 *
 * Real Google/LinkedIn logo marks aren't available here, so each button shows a small
 * flat monogram dot in that provider's well-known brand color instead — not an official
 * logo, just a recognizable stand-in next to the provider name.
 */
@Composable
fun SocialSignInButtons(
    onGoogleClick: () -> Unit,
    onLinkedInClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SocialButton(
            label = "Google",
            monogram = "G",
            monogramColor = Color(0xFFEA4335),
            onClick = onGoogleClick,
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
        SocialButton(
            label = "LinkedIn",
            monogram = "in",
            monogramColor = Color(0xFF0A66C2),
            onClick = onLinkedInClick,
            enabled = enabled,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SocialButton(
    label: String,
    monogram: String,
    monogramColor: Color,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(percent = 50),
        border = BorderStroke(1.dp, SkillXColors.TextSecondary.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier.size(20.dp).background(monogramColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(monogram, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
        }
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, color = SkillXColors.TextPrimary)
    }
}
