package com.skillx.features.safety.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.SectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafetyScreen(onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Safety Guidelines") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
            item {
                Text("Stay Safe on SkillX", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                listOf("Meet in public or institution-approved places.", "Do not share passwords, financial details, or other sensitive information.", "Keep lesson communication respectful and focused on the agreed skill.", "If a user behaves inappropriately or you feel unsafe, stop the interaction and report the user.", "Do not send money directly to another student. SkillX points are handled by the platform.", "Only mark a lesson completed after the lesson has actually taken place.").forEach { rule ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) { Text("• $rule", modifier = Modifier.padding(16.dp)) }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text("SkillX is designed for peer learning. Use good judgment and follow your institution's safety policies.", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowSkillXWorksScreen(onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("How SkillX Works") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            item { Text("Learn from students. Teach what you know.", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Spacer(modifier = Modifier.height(6.dp)); Text("SkillX connects students based on the skills they want to learn and the skills other students can teach.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            val steps = listOf("1" to ("Create your profile" to "Add the skills you can teach and the skills you want to learn."), "2" to ("Find a match" to "SkillX compares your learning skills with other students' teaching skills."), "3" to ("Request a lesson" to "Choose a matched student and send a request for the skill you want to learn."), "4" to ("Complete the lesson" to "The teacher accepts the request and marks it completed after the lesson happens."), "5" to ("Exchange a point" to "The learner spends 1 point and the teacher earns 1 point after completion."), "6" to ("Rate and stay safe" to "Participants can rate completed lessons. Use public or institution-approved meeting places and report unsafe behaviour."))
            steps.forEach { (num, titleDesc) -> item { SectionCard { Text("$num. ${titleDesc.first}", style = MaterialTheme.typography.titleMedium); Text(titleDesc.second, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
            item { SectionCard { Text("Point Rules", style = MaterialTheme.typography.titleLarge); Text("• New users start with 5 points."); Text("• 1 completed lesson costs the learner 1 point."); Text("• The teacher receives 1 point after completion."); Text("• Points cannot go below zero."); Text("• Additional points can be purchased through RevenueCat.") } }
        }
    }
}
