package com.skillx.features.notifications.presentation.inbox
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.SkillXTopBar

@Composable
fun NotificationInboxScreen(onBack: () -> Unit) {
    Scaffold(topBar = { SkillXTopBar(title = "Notifications", onBack = onBack) }) { pv ->
        Column(modifier = Modifier.fillMaxSize().padding(pv).padding(24.dp)) {
            Text("No new notifications.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
