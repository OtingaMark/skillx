package com.skillx.features.users.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.PrimaryButton
import com.skillx.designsystem.components.SecondaryButton
import com.skillx.designsystem.components.SkillAvatar
import com.skillx.designsystem.theme.SkillXColors
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit, onNavigateToSkills: () -> Unit,
    onNavigateToMatches: () -> Unit, onNavigateToLessons: () -> Unit,
    onNavigateToSafety: () -> Unit, onNavigateToHowItWorks: () -> Unit,
    onNavigateToRevenueCat: () -> Unit, onLogout: () -> Unit,
    viewModel: HomeViewModel = koinInject()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(containerColor = SkillXColors.Background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // Top bar: wordmark, points badge, notifications, avatar
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("SkillX", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SkillXColors.Primary)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .background(SkillXColors.Accent.copy(alpha = 0.15f), RoundedCornerShape(percent = 50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("${state.pointBalance}", color = SkillXColors.Accent, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    }
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = SkillXColors.TextSecondary)
                    val initial = state.user?.name?.trim()?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(SkillXColors.Primary, CircleShape)
                            .clip(CircleShape)
                            .clickableSimple(onNavigateToProfile),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(initial, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                // Hero banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(SkillXColors.DeepIndigoStart, SkillXColors.DeepIndigoEnd)),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            "Share Skills. Grow Together.",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Real people. Real skills. Real opportunities.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateToMatches,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = SkillXColors.DeepIndigoEnd),
                            shape = RoundedCornerShape(percent = 50)
                        ) {
                            Text("Find a Match", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Recommended for You — real featured skills, no fabricated ratings
                // (HomeUiState.featuredSkills is just names; no rating data is modeled).
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Recommended for You", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SkillXColors.TextPrimary)
                    Text("See All", color = SkillXColors.Primary, style = MaterialTheme.typography.labelLarge, modifier = Modifier.clickableSimple(onNavigateToSkills))
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (state.featuredSkills.isEmpty()) {
                    Text(
                        if (state.isLoading) "Loading…" else "No recommendations yet.",
                        color = SkillXColors.TextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.featuredSkills) { skill ->
                            Card(
                                modifier = Modifier.width(120.dp).clickableSimple(onNavigateToSkills),
                                colors = CardDefaults.cardColors(containerColor = SkillXColors.Surface),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    SkillAvatar(name = skill, size = 48.dp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(skill, style = MaterialTheme.typography.labelLarge, color = SkillXColors.TextPrimary, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Quick links — every navigation entry point this screen already supported,
                // kept reachable now that the hero above covers only "Find a Match".
                Text("More", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SkillXColors.TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PrimaryButton(text = "My Skills", onClick = onNavigateToSkills)
                    PrimaryButton(text = "Lesson Requests", onClick = onNavigateToLessons)
                    SecondaryButton(text = "Buy Points", onClick = onNavigateToRevenueCat)
                    SecondaryButton(text = "How SkillX Works", onClick = onNavigateToHowItWorks)
                    SecondaryButton(text = "Safety Guidelines", onClick = onNavigateToSafety)
                    SecondaryButton(text = "Logout", onClick = onLogout)
                }

                if (state.error.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(state.error, color = SkillXColors.Error, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

private fun Modifier.clickableSimple(onClick: () -> Unit): Modifier =
    this.clickable(onClick = onClick)
