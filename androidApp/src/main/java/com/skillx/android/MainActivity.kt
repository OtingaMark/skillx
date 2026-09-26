package com.skillx.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.skillx.designsystem.theme.SkillXTheme
import com.skillx.navigation.graph.SkillXNavGraph
import com.skillx.navigation.navigator.AppNavigator
import org.koin.android.ext.android.inject

/**
 * Thin Android entry point — under 30 lines.
 * All business logic, UI, and navigation live in :shared.
 */
class MainActivity : ComponentActivity() {
    private val navigator: AppNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillXTheme {
                SkillXNavGraph(navigator = navigator)
            }
        }
    }
}
