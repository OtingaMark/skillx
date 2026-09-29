package com.skillx.features.skills.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import org.koin.compose.koinInject
import com.skillx.features.skills.presentation.skills.SkillsViewModel
import com.skillx.features.skills.presentation.skills.component.SkillRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillsScreen(
    onBack: () -> Unit
) {
    val viewModel: SkillsViewModel = koinInject()
    val uiState by viewModel.uiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    val popularSkills = listOf(
        "Programming",
        "Graphic Design",
        "Photography",
        "Public Speaking",
        "Marketing",
        "Cooking",
        "Video Editing",
        "Web Design"
    )

    val filteredTeachSkills = uiState.teachSkills.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    val filteredLearnSkills = uiState.learnSkills.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopAppBar(
                title = {
                    Text("My Skills")
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    placeholder = {
                        Text("Search skills")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    )
                )

                Text("Popular Skills")

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popularSkills.forEach { skill ->
                        FilterChip(
                            selected = false,
                            onClick = {
                                searchQuery = skill
                            },
                            label = {
                                Text(skill)
                            }
                        )
                    }
                }
            }
        }

        item {
            SkillSectionCard(
                title = "Skills I Can Teach",
                skills = filteredTeachSkills,
                emptyText = "You haven't added any teaching skills yet.",
                newSkill = uiState.newTeachSkill,
                onNewSkillChanged = viewModel::onNewTeachSkillChanged,
                onAdd = viewModel::addTeachingSkill,
                onRemove = viewModel::removeTeachingSkill
            )
        }

        item {
            SkillSectionCard(
                title = "Skills I Want to Learn",
                skills = filteredLearnSkills,
                emptyText = "You haven't added any learning skills yet.",
                newSkill = uiState.newLearnSkill,
                onNewSkillChanged = viewModel::onNewLearnSkillChanged,
                onAdd = viewModel::addLearningSkill,
                onRemove = viewModel::removeLearningSkill
            )
        }

        if (uiState.error.isNotBlank()) {
            item {
                Text(
                    text = uiState.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun SkillSectionCard(
    title: String,
    skills: List<String>,
    emptyText: String,
    newSkill: String,
    onNewSkillChanged: (String) -> Unit,
    onAdd: () -> Unit,
    onRemove: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = title)

            if (skills.isEmpty()) {
                Text(text = emptyText)
            } else {
                skills.forEach { skill ->
                    SkillRow(
                        skill = skill,
                        onRemove = {
                            onRemove(skill)
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newSkill,
                    onValueChange = onNewSkillChanged,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = {
                        Text("Add a skill")
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onAdd) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add skill"
                    )
                }
            }
        }
    }
}
