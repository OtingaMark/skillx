package com.skillx.features.lessons.presentation.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.presentation.list.component.LessonRequestCard

/**
 * Screen displaying the user's lesson requests.
 * Shows two tabs: "Learning" (requests made by user) and "Teaching" (requests received by user).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonRequestsScreen(
    viewModel: LessonRequestsViewModel,
    onBack: () -> Unit,
    onRateLesson: (LessonRequest) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val tabs = listOf("Learning", "Teaching")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Lesson Requests") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            TabRow(selectedTabIndex = tabs.indexOf(uiState.selectedTab).coerceAtLeast(0)) {
                tabs.forEach { tab ->
                    Tab(
                        selected = uiState.selectedTab == tab,
                        onClick = { viewModel.onTabSelected(tab) },
                        text = { Text(tab) }
                    )
                }
            }

            when {
                uiState.isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                uiState.error.isNotBlank() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(uiState.error, color = MaterialTheme.colorScheme.error)
                }
                uiState.requests.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No lesson requests yet.")
                }
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.requests) { request ->
                        LessonRequestCard(
                            request = request,
                            onAccept = { viewModel.acceptRequest(request.id) },
                            onRate = { onRateLesson(request) }
                        )
                    }
                }
            }
        }
    }
}
