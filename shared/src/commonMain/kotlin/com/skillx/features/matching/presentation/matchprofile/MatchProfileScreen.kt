package com.skillx.features.matching.presentation.matchprofile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SecondaryButton
import com.skillx.features.matching.domain.model.SkillMatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchProfileScreen(match: SkillMatch, onBack: () -> Unit, onRequestLesson: () -> Unit, onReportUser: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Student Profile") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
            item {
                Text(match.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp)); Text(match.email)
                Spacer(modifier = Modifier.height(24.dp)); Text("Skill You Want to Learn", style = MaterialTheme.typography.titleMedium); Spacer(modifier = Modifier.height(8.dp)); Text("• ${match.matchedSkill}")
                Spacer(modifier = Modifier.height(24.dp)); Text("Skills They Teach", style = MaterialTheme.typography.titleMedium); Spacer(modifier = Modifier.height(8.dp))
                match.teachSkills.forEach { Text("• $it") }
                Spacer(modifier = Modifier.height(24.dp)); Text("Skills They Want to Learn", style = MaterialTheme.typography.titleMedium); Spacer(modifier = Modifier.height(8.dp))
                if (match.learnSkills.isEmpty()) Text("No learning skills listed.") else match.learnSkills.forEach { Text("• $it") }
                Spacer(modifier = Modifier.height(32.dp)); PrimaryButton(text = "Request a Lesson", onClick = onRequestLesson)
                Spacer(modifier = Modifier.height(12.dp)); SecondaryButton(text = "Report User", onClick = onReportUser)
            }
        }
    }
}
