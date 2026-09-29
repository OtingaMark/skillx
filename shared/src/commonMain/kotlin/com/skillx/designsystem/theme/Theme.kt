package com.skillx.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * SkillX Material 3 theme composable — the single entry point for theming.
 * Extracted from the private SkillXTheme composable in MainActivity.kt L123-148.
 *
 * `darkTheme` here only drives `MaterialTheme.colorScheme` (used by default-styled
 * Material components, e.g. input text color, icon tint). The screens themselves render
 * via `SkillXColors` (see Color.kt), which independently branches on the same
 * `isSystemInDarkTheme()` signal — both must agree, which is why this defaults to the
 * same system signal rather than a fixed value.
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
