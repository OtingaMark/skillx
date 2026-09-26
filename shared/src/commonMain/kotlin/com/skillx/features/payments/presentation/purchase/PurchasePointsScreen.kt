package com.skillx.features.payments.presentation.purchase
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.*
import com.skillx.designsystem.theme.*
import com.skillx.features.payments.domain.model.PointPackage

@Composable
fun PurchasePointsScreen(onBack: () -> Unit, onViewHistory: () -> Unit) {
    Scaffold(topBar = { SkillXTopBar(title = "Buy Points", onBack = onBack) }) { pv ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(pv).padding(16.dp)) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) { Text("Your Points", style = MaterialTheme.typography.bodyLarge); Text("5", style = MaterialTheme.typography.displayMedium) }
                        SecondaryButton(text = "View History", onClick = onViewHistory, modifier = Modifier.width(140.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Choose a Package", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(listOf(PointPackage("p10", 10, "$1.99"), PointPackage("p25", 25, "$4.99"), PointPackage("p50", 50, "$8.99"), PointPackage("p100", 100, "$14.99"))) { pkg ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("💰", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) { Text("${pkg.points} Points", style = MaterialTheme.typography.titleMedium); Text(pkg.formattedPrice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = SkillXPrimary)) { Text("Buy") }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Why buy points?", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("✓ Access premium skills", style = MaterialTheme.typography.bodyMedium)
                Text("✓ Get more lesson matches", style = MaterialTheme.typography.bodyMedium)
                Text("✓ Support our community", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
