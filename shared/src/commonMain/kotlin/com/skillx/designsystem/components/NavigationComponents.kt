package com.skillx.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Bottom navigation bar matching the design spec — Home, Search, Buy Points, Profile.
 */
@Composable
fun SkillXBottomNavigation(
    currentTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        BottomNavTab.entries.forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = { onTabSelected(tab) },
                icon = { Text(tab.icon, style = MaterialTheme.typography.titleLarge) },
                label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

enum class BottomNavTab(val label: String, val icon: String) {
    HOME("Home", "🏠"),
    SEARCH("Search", "🔍"),
    BUY_POINTS("Buy Points", "💰"),
    PROFILE("Profile", "👤")
}

/**
 * Top navigation header with optional back button and actions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillXTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Text("←", style = MaterialTheme.typography.titleLarge)
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

/**
 * Skill card component from the design — shows skill name, description, icon.
 */
@Composable
fun SkillCard(
    skillName: String,
    description: String,
    points: Int? = null,
    learners: Int? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(skillName, style = MaterialTheme.typography.titleMedium)
                if (description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (points != null || learners != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        points?.let { Text("🔥 $it pts", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        if (points != null && learners != null) { Spacer(modifier = Modifier.width(8.dp)); Text("•", color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        learners?.let { Spacer(modifier = Modifier.width(8.dp)); Text("$it learners", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                }
            }
            Text("›", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/**
 * Rating summary card — shows star rating with numeric value.
 */
@Composable
fun RatingCard(
    rating: Double,
    totalRatings: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        RatingBar(rating = rating)
        Spacer(modifier = Modifier.width(8.dp))
        Text("${String.format("%.1f", rating)}", style = MaterialTheme.typography.titleMedium)
        if (totalRatings > 0) {
            Spacer(modifier = Modifier.width(4.dp))
            Text("($totalRatings)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
