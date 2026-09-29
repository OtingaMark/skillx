package com.skillx.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * SkillX color palette — matches the "Color Palette & UI Style" panel of the product mockup.
 *
 * Primary     #6566F1  Indigo
 * Secondary   #06B6D4  Cyan
 * Accent      #F59E0B  Amber
 * Success     #10B981  Green
 * Warning     #F97316  Orange
 * Error       #EF4444  Red
 * Background  #F8FAFC  Light
 * Surface     #FFFFFF  Card
 */

// Brand palette
val SkillXPrimary = Color(0xFF6566F1)
val SkillXOnPrimary = Color.White
val SkillXPrimaryContainer = Color(0xFFE0E0FF)
val SkillXOnPrimaryContainer = Color(0xFF1A1B4B)

val SkillXSecondary = Color(0xFF06B6D4)
val SkillXOnSecondary = Color.White
val SkillXSecondaryContainer = Color(0xFFCFFAFE)
val SkillXOnSecondaryContainer = Color(0xFF083344)

val SkillXTertiary = Color(0xFFF59E0B)
val SkillXOnTertiary = Color.White
val SkillXTertiaryContainer = Color(0xFFFEF3C7)
val SkillXOnTertiaryContainer = Color(0xFF451A03)

val SkillXAccent = Color(0xFFF59E0B)
val SkillXSuccess = Color(0xFF10B981)
val SkillXWarning = Color(0xFFF97316)

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

// Onboarding-specific colors (exact values from mockup spec)
val SkillXOnboardingPrimary = Color(0xFF6566F1)    // Indigo - primary buttons, active step
val SkillXOnboardingSecondary = Color(0xFF06B6D4)  // Cyan
val SkillXOnboardingAccent = Color(0xFFF59E0B)     // Amber
val SkillXOnboardingSuccess = Color(0xFF10B981)    // Green - checkmarks
val SkillXOnboardingWarning = Color(0xFFF97316)    // Orange
val SkillXOnboardingError = Color(0xFFEF4444)      // Red - validation errors
val SkillXOnboardingBackground = Color(0xFFF8FAFC) // Screen background
val SkillXOnboardingSurface = Color(0xFFFFFFFF)    // Cards, inputs
val SkillXOnboardingTextPrimary = Color(0xFF0F172A)  // Headlines, labels
val SkillXOnboardingTextSecondary = Color(0xFF647488) // Supporting copy

// Deep indigo/violet used for the Welcome screen and Home hero banner — a noticeably
// darker step in the same hue family as Primary, matching the supplied mockup. Also
// doubles as the dark-mode background gradient start, since it's already near-black-purple.
val SkillXDeepIndigoStart = Color(0xFF2E1065)
val SkillXDeepIndigoEnd = Color(0xFF4C1D95)

// Small fixed palette for deterministic per-skill avatar colors (mockup shows each
// skill/language chip with its own flat accent square — no real per-skill icon art
// exists, so a name-hash picks one of these consistently). Same palette works on both
// themes — these are saturated mid-tones that read fine against light or dark surfaces.
val SkillXSkillAvatarPalette = listOf(
    Color(0xFF6566F1), // indigo
    Color(0xFFF59E0B), // amber
    Color(0xFF06B6D4), // cyan
    Color(0xFF10B981), // green
    Color(0xFFEF4444), // red
    Color(0xFF8B5CF6), // violet
    Color(0xFFF97316), // orange
    Color(0xFFEC4899)  // pink
)

// Dark-mode counterparts for the onboarding/auth palette above — a deliberate dark
// adaptation (not just Material defaults), keeping the same brand hues but shifted for
// readability on dark surfaces: brighter primary/accents, deep slate backgrounds.
val SkillXOnboardingDarkPrimary = Color(0xFFA5B4FC)      // lighter indigo — enough contrast on dark
val SkillXOnboardingDarkSecondary = Color(0xFF22D3EE)    // brighter cyan
val SkillXOnboardingDarkAccent = Color(0xFFFBBF24)       // brighter amber
val SkillXOnboardingDarkSuccess = Color(0xFF34D399)
val SkillXOnboardingDarkWarning = Color(0xFFFB923C)
val SkillXOnboardingDarkError = Color(0xFFF87171)
val SkillXOnboardingDarkBackground = Color(0xFF0B1120)   // near-black slate
val SkillXOnboardingDarkSurface = Color(0xFF1E293B)      // card/input surface, one step up
val SkillXOnboardingDarkTextPrimary = Color(0xFFF1F5F9)
val SkillXOnboardingDarkTextSecondary = Color(0xFF94A3B8)
val SkillXDeepIndigoStartDark = Color(0xFF1E0A47)        // slightly deeper for dark-mode hero banners
val SkillXDeepIndigoEndDark = Color(0xFF3B1370)

/**
 * Semantic color tokens for onboarding/auth/home screens. Every property is theme-aware
 * (branches on [isSystemInDarkTheme]) so screens that reference `SkillXColors.X` render
 * correctly in both themes automatically — the single source of truth for this decision
 * lives here, not duplicated per screen.
 */
object SkillXColors {
    val Primary: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkPrimary else SkillXOnboardingPrimary
    val Secondary: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkSecondary else SkillXOnboardingSecondary
    val Accent: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkAccent else SkillXOnboardingAccent
    val Success: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkSuccess else SkillXOnboardingSuccess
    val Warning: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkWarning else SkillXOnboardingWarning
    val Error: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkError else SkillXOnboardingError
    val Background: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkBackground else SkillXOnboardingBackground
    val Surface: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkSurface else SkillXOnboardingSurface
    val TextPrimary: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkTextPrimary else SkillXOnboardingTextPrimary
    val TextSecondary: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkTextSecondary else SkillXOnboardingTextSecondary
    val PrimaryContainer: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkPrimary.copy(alpha = 0.20f) else SkillXPrimaryContainer.copy(alpha = 0.15f)
    val OnPrimary: Color @Composable get() = if (isSystemInDarkTheme()) SkillXOnboardingDarkBackground else SkillXOnPrimary
    val DeepIndigoStart: Color @Composable get() = if (isSystemInDarkTheme()) SkillXDeepIndigoStartDark else SkillXDeepIndigoStart
    val DeepIndigoEnd: Color @Composable get() = if (isSystemInDarkTheme()) SkillXDeepIndigoEndDark else SkillXDeepIndigoEnd
    val SkillAvatarPalette = SkillXSkillAvatarPalette
}

// Dark theme colors
val SkillXDarkPrimary = Color(0xFFA5B4FC)
val SkillXDarkOnPrimary = Color(0xFF312E81)
val SkillXDarkPrimaryContainer = Color(0xFF4338CA)
val SkillXDarkSecondary = Color(0xFF22D3EE)
val SkillXDarkOnSecondary = Color(0xFF083344)
val SkillXDarkTertiary = Color(0xFFFBBF24)
val SkillXDarkOnTertiary = Color(0xFF451A03)
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
    tertiary = SkillXDarkTertiary,
    onTertiary = SkillXDarkOnTertiary,
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
