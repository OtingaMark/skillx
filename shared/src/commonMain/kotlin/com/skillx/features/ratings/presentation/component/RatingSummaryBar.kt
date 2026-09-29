package com.skillx.features.ratings.presentation.component
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.core.extensions.toOneDecimalString
import com.skillx.designsystem.components.RatingBar
import com.skillx.designsystem.theme.*

@Composable
fun RatingSummaryBar(averageRating: Double?, totalRatings: Int, modifier: Modifier = Modifier) {
    if (averageRating == null) return
    Card(modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            RatingBar(rating = averageRating)
            Spacer(modifier = Modifier.width(8.dp))
            Text(averageRating.toOneDecimalString(), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(4.dp))
            Text("($totalRatings ratings)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
