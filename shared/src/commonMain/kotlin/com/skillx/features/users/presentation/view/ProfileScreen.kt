package com.skillx.features.users.presentation.view
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit, onEdit: () -> Unit) {
    Scaffold(topBar = { SkillXTopBar(title = "Profile", onBack = onBack) }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👤", style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text("Alex Carter", style = MaterialTheme.typography.headlineMedium)
            Text("@alexcarter", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Edit Profile", onClick = onEdit)
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("5", style = MaterialTheme.typography.titleLarge); Text("Points", style = MaterialTheme.typography.labelSmall) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("12", style = MaterialTheme.typography.titleLarge); Text("Skills Taught", style = MaterialTheme.typography.labelSmall) }
                Column(horizontalAlignment = Alignment.CenterHorizontally) { Text("8", style = MaterialTheme.typography.titleLarge); Text("Skills Learned", style = MaterialTheme.typography.labelSmall) }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Your Skills", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
        }
    }
}
