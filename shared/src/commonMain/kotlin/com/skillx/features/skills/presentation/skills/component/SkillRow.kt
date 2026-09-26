package com.skillx.features.skills.presentation.skills.component
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkillRow(skill: String, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("•", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 8.dp))
        Text(skill, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        IconButton(onClick = onRemove) { Text("✕", color = MaterialTheme.colorScheme.error) }
    }
}
