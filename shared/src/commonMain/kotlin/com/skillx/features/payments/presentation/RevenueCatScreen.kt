package com.skillx.features.payments.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RevenueCatScreen(onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Buy Points") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
            Text("Get More SkillX Points", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp)); Text("Purchase additional points to request more lessons.")
            Spacer(modifier = Modifier.height(24.dp)); Text("Loading available packages...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
