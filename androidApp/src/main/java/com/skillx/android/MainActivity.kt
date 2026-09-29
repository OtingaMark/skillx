package com.skillx.android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.skillx.designsystem.theme.SkillXTheme
import com.skillx.features.authentication.platform.LinkedInOAuthCallbackRegistry
import com.skillx.navigation.graph.SkillXNavGraph
import com.skillx.navigation.navigator.AppNavigator
import org.koin.android.ext.android.inject

/**
 * Thin Android entry point.
 * All business logic, UI, and navigation live in :shared.
 */
class MainActivity : ComponentActivity() {
    private val navigator: AppNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleLinkedInRedirect(intent)
        enableEdgeToEdge()
        setContent {
            SkillXTheme {
                SkillXNavGraph(navigator = navigator)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleLinkedInRedirect(intent)
    }

    private fun handleLinkedInRedirect(intent: Intent) {
        intent.data?.let { LinkedInOAuthCallbackRegistry.complete(it) }
    }
}
