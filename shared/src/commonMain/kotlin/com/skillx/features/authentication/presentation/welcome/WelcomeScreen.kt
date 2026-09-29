package com.skillx.features.authentication.presentation.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SkillIllustration
import com.skillx.designsystem.theme.SkillXColors

@Composable
fun WelcomeScreen(
    onCreateAccount: () -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(SkillXColors.DeepIndigoStart, SkillXColors.DeepIndigoEnd)
                )
            )
            // Background stays edge-to-edge under the status bar (matches the mockup);
            // only the content below is inset, since statusBarsPadding() is applied after
            // the background draws at the full, un-padded bounds.
            .statusBarsPadding()
            .padding(horizontal = 28.dp),
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "SkillX",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Share Skills. Grow Together.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            SkillIllustration(avatarColor = Color.White, avatarIconTint = SkillXColors.DeepIndigoEnd)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Learn from others.\nTeach what you know.\nEarn points. Build your future.",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(text = "Get Started", onClick = onCreateAccount)

        TextButton(onClick = onLogin, modifier = Modifier.fillMaxWidth()) {
            Text("Already have an account? Log In", color = Color.White.copy(alpha = 0.85f))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
