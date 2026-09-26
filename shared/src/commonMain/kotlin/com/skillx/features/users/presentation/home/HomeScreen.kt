package com.skillx.features.users.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SecondaryButton
import com.skillx.designsystem.components.SectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit, onNavigateToSkills: () -> Unit,
    onNavigateToMatches: () -> Unit, onNavigateToLessons: () -> Unit,
    onNavigateToSafety: () -> Unit, onNavigateToHowItWorks: () -> Unit,
    onNavigateToRevenueCat: () -> Unit, onLogout: () -> Unit
) {
    Scaffold(topBar = { TopAppBar(title = { Text("SkillX") }) }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
            Text(text = "Welcome back!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            SectionCard { Text("Your Dashboard", style = MaterialTheme.typography.titleLarge); Spacer(modifier = Modifier.height(8.dp)); Text("Manage your skills and find learning partners.") }
            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(text = "My Profile", onClick = onNavigateToProfile)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "My Skills", onClick = onNavigateToSkills)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Find a Skill", onClick = onNavigateToMatches)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Lesson Requests", onClick = onNavigateToLessons)
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Buy Points", onClick = onNavigateToRevenueCat)
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "How SkillX Works", onClick = onNavigateToHowItWorks)
            Spacer(modifier = Modifier.height(8.dp))
            SecondaryButton(text = "Safety Guidelines", onClick = onNavigateToSafety)
            Spacer(modifier = Modifier.height(24.dp))
            SecondaryButton(text = "Logout", onClick = onLogout)
        }
    }
}
