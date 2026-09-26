package com.skillx.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * SkillX Material 3 theme composable — the single entry point for theming.
 * Extracted from the private SkillXTheme composable in MainActivity.kt L123-148.
 */
@Composable
fun SkillXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SkillXDarkColors else SkillXLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SkillXTypography,
        shapes = SkillXShapes,
        content = content
    )
}
