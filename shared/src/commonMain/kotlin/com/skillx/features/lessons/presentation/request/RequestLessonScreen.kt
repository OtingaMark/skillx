package com.skillx.features.lessons.presentation.request

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SectionCard
import com.skillx.features.matching.domain.model.SkillMatch

/**
 * Screen for requesting a lesson from a matched user.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestLessonScreen(
    viewModel: RequestLessonViewModel,
    match: SkillMatch,
    onBack: () -> Unit,
    onRequestSent: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onRequestSent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Request Lesson") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SectionCard {
                Text(text = "Request lesson from ${uiState.teacherName}", style = MaterialTheme.typography.titleLarge)
                Text(text = "Skill: ${uiState.skill}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = if (uiState.isSending) "Sending..." else "Send Request",
                    onClick = { viewModel.onSendRequest() },
                    enabled = !uiState.isSending
                )
            }

            if (uiState.error.isNotBlank()) {
                Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
