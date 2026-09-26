package com.skillx.features.lessons.presentation.list.component
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.model.LessonStatus
import com.skillx.designsystem.theme.*

@Composable
fun LessonRequestCard(request: LessonRequest, onAccept: (() -> Unit)? = null, onComplete: (() -> Unit)? = null, onRate: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(request.skill, style = MaterialTheme.typography.titleMedium)
                val (statusText, statusColor) = when (request.status) {
                    LessonStatus.PENDING -> "Pending" to SkillXWarning; LessonStatus.ACCEPTED -> "In Progress" to SkillXAccent; LessonStatus.COMPLETED -> "Completed" to SkillXSuccess
                }
                Text(statusText, style = MaterialTheme.typography.labelMedium, color = statusColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Teacher: ${request.teacherName}", style = MaterialTheme.typography.bodyMedium)
            Text("Student: ${request.requesterName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (request.status == LessonStatus.PENDING && onAccept != null) { Spacer(modifier = Modifier.height(12.dp)); Button(onClick = onAccept) { Text("Accept") } }
            if (request.status == LessonStatus.COMPLETED && onRate != null) { Spacer(modifier = Modifier.height(12.dp)); OutlinedButton(onClick = onRate) { Text("Rate Teacher") } }
        }
    }
}
