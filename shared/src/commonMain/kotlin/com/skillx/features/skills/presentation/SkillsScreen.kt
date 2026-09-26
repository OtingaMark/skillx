package com.skillx.features.skills.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(onBack: () -> Unit) {
    var newTeachSkill by remember { mutableStateOf("") }
    var newLearnSkill by remember { mutableStateOf("") }
    var teachSkills by remember { mutableStateOf<List<String>>(emptyList()) }
    var learnSkills by remember { mutableStateOf<List<String>>(emptyList()) }

    Scaffold(topBar = { TopAppBar(title = { Text("My Skills") }, navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }) }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)) {
            Text("Skills I Can Teach", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = newTeachSkill, onValueChange = { newTeachSkill = it }, label = { Text("Add a teaching skill") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Add Teaching Skill", onClick = { if (newTeachSkill.isNotBlank()) { teachSkills = teachSkills + newTeachSkill.trim(); newTeachSkill = "" } })
            teachSkills.forEach { skill -> Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(skill, modifier = Modifier.weight(1f)); TextButton(onClick = { teachSkills = teachSkills.filter { it != skill } }) { Text("Remove") } } }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Skills I Want to Learn", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = newLearnSkill, onValueChange = { newLearnSkill = it }, label = { Text("Add a learning skill") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            PrimaryButton(text = "Add Learning Skill", onClick = { if (newLearnSkill.isNotBlank()) { learnSkills = learnSkills + newLearnSkill.trim(); newLearnSkill = "" } })
            learnSkills.forEach { skill -> Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(skill, modifier = Modifier.weight(1f)); TextButton(onClick = { learnSkills = learnSkills.filter { it != skill } }) { Text("Remove") } } }
        }
    }
}
