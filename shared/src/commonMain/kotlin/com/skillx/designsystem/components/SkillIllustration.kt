package com.skillx.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors

/**
 * Stand-in illustration for the "person at laptop with floating skill-category icons"
 * artwork in the mockup — no real illustration/photo assets exist, so this is a simple
 * Compose-drawn substitute reused across Welcome/Login/SignUp/the onboarding intro.
 */
@Composable
fun SkillIllustration(
    modifier: Modifier = Modifier,
    avatarColor: Color = SkillXColors.Primary,
    avatarIconTint: Color = Color.White
) {
    Box(
        modifier = modifier.size(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .background(avatarColor.copy(alpha = 0.15f), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(avatarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = avatarIconTint)
        }
        FloatingBadge(
            icon = Icons.Default.Laptop,
            color = SkillXColors.Secondary,
            modifier = Modifier.align(Alignment.TopStart).offset(x = 4.dp, y = 8.dp)
        )
        FloatingBadge(
            icon = Icons.Default.Chat,
            color = SkillXColors.Accent,
            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = 0.dp)
        )
        FloatingBadge(
            icon = Icons.Default.PhotoCamera,
            color = SkillXColors.Success,
            modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-8).dp, y = (-4).dp)
        )
    }
}

@Composable
private fun FloatingBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(36.dp)
            .background(color, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
    }
}
