package com.skillx.features.matching.presentation.matches

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.features.matching.domain.model.SkillMatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchesScreen(onBack: () -> Unit, onViewProfile: (SkillMatch) -> Unit) {
    var matches by remember { mutableStateOf<List<SkillMatch>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    Scaffold(topBar = { TopAppBar(title = { Text("Find a Skill") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { paddingValues ->
        when {
            loading -> Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("Finding students...") }
            matches.isEmpty() -> Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) { Text("No matches yet.", style = MaterialTheme.typography.titleLarge); Spacer(modifier = Modifier.height(8.dp)); Text("Add skills you want to learn and wait for another student who can teach them.") }
            else -> LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
                items(matches) { match ->
                    Card(modifier = Modifier.fillMaxWidth()) { Column(modifier = Modifier.padding(16.dp)) { Text(match.name, style = MaterialTheme.typography.titleLarge); Spacer(modifier = Modifier.height(8.dp)); Text("Can teach: ${match.matchedSkill}"); Spacer(modifier = Modifier.height(8.dp)); Text("Email: ${match.email}"); Spacer(modifier = Modifier.height(12.dp)); PrimaryButton(text = "View Profile", onClick = { onViewProfile(match) }) } }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
