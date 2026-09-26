package com.skillx.designsystem.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

/**
 * SkillX color palette — synchronized with brand design spec.
 *
 * Primary (Brand)   #6366F1  Indigo
 * Secondary         #8B5CF6  Violet
 * Accent            #06B6D4  Cyan
 * Success (Green)   #10B981
 * Warning (Orange)  #F59E0B
 * Error (Red)       #EF4444
 * Background        #F8FAFC
 * Surface           #FFFFFF
 * Text Primary      #0F172A
 * Text Secondary    #64748B
 */

// Brand palette
val SkillXPrimary = Color(0xFF6366F1)
val SkillXOnPrimary = Color.White
val SkillXPrimaryContainer = Color(0xFFE0E0FF)
val SkillXOnPrimaryContainer = Color(0xFF1A1B4B)

val SkillXSecondary = Color(0xFF8B5CF6)
val SkillXOnSecondary = Color.White
val SkillXSecondaryContainer = Color(0xFFEDE9FE)
val SkillXOnSecondaryContainer = Color(0xFF2E1065)

val SkillXTertiary = Color(0xFF06B6D4)
val SkillXOnTertiary = Color.White
val SkillXTertiaryContainer = Color(0xFFCFFAFE)
val SkillXOnTertiaryContainer = Color(0xFF083344)

val SkillXAccent = Color(0xFF06B6D4)
val SkillXSuccess = Color(0xFF10B981)
val SkillXWarning = Color(0xFFF59E0B)

val SkillXError = Color(0xFFEF4444)
val SkillXOnError = Color.White
val SkillXErrorContainer = Color(0xFFFEE2E2)

val SkillXBackground = Color(0xFFF8FAFC)
val SkillXOnBackground = Color(0xFF0F172A)
val SkillXSurface = Color(0xFFFFFFFF)
val SkillXOnSurface = Color(0xFF0F172A)
val SkillXSurfaceVariant = Color(0xFFF1F5F9)
val SkillXOnSurfaceVariant = Color(0xFF64748B)
val SkillXOutline = Color(0xFFCBD5E1)

// Text colors
val SkillXTextPrimary = Color(0xFF0F172A)
val SkillXTextSecondary = Color(0xFF64748B)

// Dark theme colors
val SkillXDarkPrimary = Color(0xFFA5B4FC)
val SkillXDarkOnPrimary = Color(0xFF312E81)
val SkillXDarkPrimaryContainer = Color(0xFF4338CA)
val SkillXDarkSecondary = Color(0xFFC4B5FD)
val SkillXDarkOnSecondary = Color(0xFF4C1D95)
val SkillXDarkBackground = Color(0xFF0F172A)
val SkillXDarkOnBackground = Color(0xFFE2E8F0)
val SkillXDarkSurface = Color(0xFF1E293B)
val SkillXDarkOnSurface = Color(0xFFE2E8F0)
val SkillXDarkSurfaceVariant = Color(0xFF334155)
val SkillXDarkOnSurfaceVariant = Color(0xFF94A3B8)

val SkillXLightColors = lightColorScheme(
    primary = SkillXPrimary,
    onPrimary = SkillXOnPrimary,
    primaryContainer = SkillXPrimaryContainer,
    onPrimaryContainer = SkillXOnPrimaryContainer,
    secondary = SkillXSecondary,
    onSecondary = SkillXOnSecondary,
    secondaryContainer = SkillXSecondaryContainer,
    onSecondaryContainer = SkillXOnSecondaryContainer,
    tertiary = SkillXTertiary,
    onTertiary = SkillXOnTertiary,
    tertiaryContainer = SkillXTertiaryContainer,
    onTertiaryContainer = SkillXOnTertiaryContainer,
    background = SkillXBackground,
    onBackground = SkillXOnBackground,
    surface = SkillXSurface,
    onSurface = SkillXOnSurface,
    surfaceVariant = SkillXSurfaceVariant,
    onSurfaceVariant = SkillXOnSurfaceVariant,
    outline = SkillXOutline,
    error = SkillXError,
    onError = SkillXOnError,
    errorContainer = SkillXErrorContainer
)

val SkillXDarkColors = darkColorScheme(
    primary = SkillXDarkPrimary,
    onPrimary = SkillXDarkOnPrimary,
    primaryContainer = SkillXDarkPrimaryContainer,
    secondary = SkillXDarkSecondary,
    onSecondary = SkillXDarkOnSecondary,
    background = SkillXDarkBackground,
    onBackground = SkillXDarkOnBackground,
    surface = SkillXDarkSurface,
    onSurface = SkillXDarkOnSurface,
    surfaceVariant = SkillXDarkSurfaceVariant,
    onSurfaceVariant = SkillXDarkOnSurfaceVariant,
    error = SkillXError,
    onError = SkillXOnError,
    errorContainer = SkillXErrorContainer
)
