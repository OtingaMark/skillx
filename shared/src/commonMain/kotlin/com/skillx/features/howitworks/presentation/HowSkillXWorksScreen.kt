package com.skillx.features.howitworks.presentation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skillx.designsystem.components.SkillXTopBar
import com.skillx.features.howitworks.presentation.component.HowItWorksStepCard

@Composable
fun HowSkillXWorksScreen(onBack: () -> Unit) {
    Scaffold(topBar = { SkillXTopBar(title = "How SkillX Works", onBack = onBack) }) { pv ->
        Column(modifier = Modifier.fillMaxSize().padding(pv).padding(24.dp)) {
            HowItWorksStepCard(step = 1, title = "Add Your Skills", description = "Tell us what you can teach and what you want to learn.")
            HowItWorksStepCard(step = 2, title = "Find Matches", description = "We'll connect you with people who teach what you want to learn.")
            HowItWorksStepCard(step = 3, title = "Request a Lesson", description = "Send a lesson request to start learning.")
            HowItWorksStepCard(step = 4, title = "Complete & Rate", description = "After the lesson, rate your teacher and earn points!")
        }
    }
}
