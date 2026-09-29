package com.skillx.features.onboarding.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skillx.designsystem.theme.SkillXColors

/**
 * Search field for finding skills in the teach/learn skill screens.
 */
@Composable
fun SkillSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "Search skills…"
) {
    var text by remember { mutableStateOf(TextFieldValue(query)) }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onQueryChange(newText.text)
        },
        label = { Text(hint, color = SkillXColors.TextSecondary) },
        leadingIcon = {
            IconButton(onClick = { focusManager.clearFocus() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SkillXColors.Primary,
            unfocusedBorderColor = SkillXColors.Primary.copy(alpha = 0.3f),
            focusedLabelColor = SkillXColors.Primary,
            unfocusedLabelColor = SkillXColors.TextSecondary,
            cursorColor = SkillXColors.Primary
        )
    )
}