package com.skillx.features.lessons.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.model.LessonStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonRequestsScreen(
    viewModel: LessonRequestsViewModel,
    onBack: () -> Unit,
    onRateLesson: (LessonRequest) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Lessons",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Manage your learning and teaching",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            TabRow(
                selectedTabIndex = if (uiState.selectedTab == "Learning") 0 else 1
            ) {

                Tab(
                    selected = uiState.selectedTab == "Learning",
                    onClick = {
                        viewModel.onTabSelected("Learning")
                    },
                    text = {
                        Text("Learning")
                    }
                )

                Tab(
                    selected = uiState.selectedTab == "Teaching",
                    onClick = {
                        viewModel.onTabSelected("Teaching")
                    },
                    text = {
                        Text("Teaching")
                    }
                )
            }

            when {

                uiState.isLoading -> {
                    LoadingContent()
                }

                uiState.error.isNotBlank() -> {
                    ErrorContent(
                        message = uiState.error,
                        onRetry = {
                            viewModel.load()
                        }
                    )
                }

                uiState.requests.isEmpty() -> {
                    EmptyLessonsContent(
                        selectedTab = uiState.selectedTab
                    )
                }

                else -> {
                    LessonRequestsContent(
                        requests = uiState.requests,
                        selectedTab = uiState.selectedTab,
                        viewModel = viewModel,
                        onRateLesson = onRateLesson
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonRequestsContent(
    requests: List<LessonRequest>,
    selectedTab: String,
    viewModel: LessonRequestsViewModel,
    onRateLesson: (LessonRequest) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        item {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (selectedTab == "Learning") {
                        "Your learning requests"
                    } else {
                        "Requests to teach"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (selectedTab == "Learning") {
                        "Track lessons you have requested from other students."
                    } else {
                        "Review students who want to learn from you."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(
            items = requests,
            key = { it.id }
        ) { request ->

            LessonRequestCard(
                request = request,
                selectedTab = selectedTab,
                viewModel = viewModel,
                onRateLesson = onRateLesson
            )
        }
    }
}

@Composable
private fun LessonRequestCard(
    request: LessonRequest,
    selectedTab: String,
    viewModel: LessonRequestsViewModel,
    onRateLesson: (LessonRequest) -> Unit
) {
    val isLearning = selectedTab == "Learning"

    val personName = if (isLearning) {
        request.teacherName
    } else {
        request.requesterName
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(46.dp)
                        .height(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = personName
                            .trim()
                            .take(1)
                            .uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = personName.ifBlank { "Student" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = request.skill,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                StatusPill(request.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(14.dp))

            when (request.status) {

                LessonStatus.PENDING -> {

                    if (isLearning) {
                        Text(
                            text = "Waiting for the teacher to accept your request.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "This student wants to learn this skill from you.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.acceptRequest(request.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Accept Lesson")
                        }
                    }
                }

                LessonStatus.ACCEPTED -> {

                    Text(
                        text = if (isLearning) {
                            "Your lesson has been accepted. You can now continue with this learning relationship."
                        } else {
                            "You accepted this learner. You can now arrange the lesson."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Text(
                                text = "Lesson accepted",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "The next step is to arrange and complete the lesson.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                LessonStatus.COMPLETED -> {

                    Text(
                        text = "This lesson has been completed.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isLearning) {
                        OutlinedButton(
                            onClick = {
                                onRateLesson(request)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Rate Lesson")
                        }
                    } else {
                        Text(
                            text = "Lesson completed successfully.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPill(
    status: LessonStatus
) {
    val text = when (status) {
        LessonStatus.PENDING -> "Pending"
        LessonStatus.ACCEPTED -> "Accepted"
        LessonStatus.COMPLETED -> "Completed"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Loading your lessons...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onRetry) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun EmptyLessonsContent(
    selectedTab: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = if (selectedTab == "Learning") {
                    "No learning requests yet"
                } else {
                    "No teaching requests yet"
                },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (selectedTab == "Learning") {
                    "When you request to learn a skill, your lesson will appear here."
                } else {
                    "When another student requests to learn from you, it will appear here."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

