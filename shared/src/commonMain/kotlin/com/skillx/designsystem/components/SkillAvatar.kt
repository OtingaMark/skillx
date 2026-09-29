package com.skillx.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors
import kotlin.math.absoluteValue

/**
 * Deterministic colored square avatar for a skill/language name — no per-skill icon art
 * exists, so this picks a stable color from the palette by hashing the name, and shows
 * its first letter. Same skill always renders the same color across screens.
 */
@Composable
fun SkillAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
) {
    val color = remember(name) { colorFor(name) }
    Box(
        modifier = modifier
            .size(size)
            .background(color, RoundedCornerShape(size / 3.5f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private fun colorFor(name: String): Color {
    val palette = SkillXColors.SkillAvatarPalette
    val index = name.trim().lowercase().hashCode().absoluteValue % palette.size
    return palette[index]
}
