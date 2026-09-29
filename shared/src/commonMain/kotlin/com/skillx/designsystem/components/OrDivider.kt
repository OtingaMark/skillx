package com.skillx.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.theme.SkillXColors

/** "or continue with" divider used on the Login/SignUp screens. */
@Composable
fun OrDivider(modifier: Modifier = Modifier, label: String = "or continue with") {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = SkillXColors.TextSecondary.copy(alpha = 0.25f))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = SkillXColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = SkillXColors.TextSecondary.copy(alpha = 0.25f))
    }
}
