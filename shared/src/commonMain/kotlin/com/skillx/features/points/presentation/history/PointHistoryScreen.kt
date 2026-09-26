package com.skillx.features.points.presentation.history
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.SkillXTopBar

@Composable
fun PointHistoryScreen(onBack: () -> Unit) {
    Scaffold(topBar = { SkillXTopBar(title = "Transaction History", onBack = onBack) }) { pv ->
        Column(modifier = Modifier.fillMaxSize().padding(pv).padding(16.dp)) {
            Text("Your transaction history will appear here.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
