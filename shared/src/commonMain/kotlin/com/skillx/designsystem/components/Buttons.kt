package com.skillx.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Tertiary tonal button — uses tertiary container colors.
 */
@Composable
fun TertiaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

/**
 * Ghost button — no background, just text.
 */
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
    }
}

/**
 * SkillX styled text field with rounded borders.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillXTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "Type here...",
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isError: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = if (label.isNotEmpty()) { { Text(label) } } else null,
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        singleLine = singleLine,
        enabled = enabled,
        isError = isError,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

/**
 * Rating bar component displaying filled/unfilled stars.
 */
@Composable
fun RatingBar(
    rating: Double,
    maxRating: Int = 5,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(modifier = modifier) {
        repeat(maxRating) { index ->
            val filled = index < rating.toInt()
            val halfFilled = !filled && index < rating
            Text(
                text = when {
                    filled -> "★"
                    halfFilled -> "★"
                    else -> "☆"
                },
                style = MaterialTheme.typography.titleLarge,
                color = if (filled || halfFilled) com.skillx.designsystem.theme.SkillXWarning
                else MaterialTheme.colorScheme.outline
            )
        }
    }
}

/**
 * Interactive rating bar for selecting a rating.
 */
@Composable
fun InteractiveRatingBar(
    selectedRating: Int,
    onRatingChanged: (Int) -> Unit,
    maxRating: Int = 5,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(modifier = modifier) {
        repeat(maxRating) { index ->
            val starIndex = index + 1
            TextButton(onClick = { onRatingChanged(starIndex) }) {
                Text(
                    text = if (starIndex <= selectedRating) "★" else "☆",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (starIndex <= selectedRating) com.skillx.designsystem.theme.SkillXWarning
                    else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

/**
 * Skill chip — used for skill tags on profiles and search.
 */
@Composable
fun SkillChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null) {
        FilterChip(
            selected = selected,
            onClick = onClick,
            label = { Text(text, style = MaterialTheme.typography.labelMedium) },
            modifier = modifier,
            shape = RoundedCornerShape(20.dp)
        )
    } else {
        AssistChip(
            onClick = {},
            label = { Text(text, style = MaterialTheme.typography.labelMedium) },
            modifier = modifier,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
