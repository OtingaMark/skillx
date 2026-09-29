package com.skillx.features.ratings.presentation.rate

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SectionCard

/**
 * Screen for rating a completed lesson.
 * Displays the lesson details and allows the user to submit a 1-5 star rating with optional feedback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateLessonScreen(
    viewModel: RateLessonViewModel,
    onBack: () -> Unit,
    onRated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onRated()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rate Lesson") },
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
                Text(text = "Rate your lesson with ${uiState.teacherName}", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Your Rating", style = MaterialTheme.typography.titleMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    (1..5).forEach { star ->
                        IconButton(onClick = { viewModel.onRatingChanged(star) }) {
                            Icon(
                                imageVector = if (star <= uiState.selectedRating) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "$star stars",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.comment,
                    onValueChange = { viewModel.onCommentChanged(it) },
                    label = { Text("Feedback (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = if (uiState.isSubmitting) "Submitting..." else "Submit Rating",
                    onClick = { viewModel.onSubmit() },
                    enabled = !uiState.isSubmitting && uiState.selectedRating > 0
                )
            }

            if (uiState.error.isNotBlank()) {
                Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
