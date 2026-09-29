package com.skillx

import androidx.compose.ui.window.ComposeUIViewController
import com.skillx.designsystem.theme.SkillXTheme
import com.skillx.di.initKoin
import com.skillx.navigation.graph.SkillXNavGraph
import com.skillx.navigation.navigator.AppNavigator
import org.koin.mp.KoinPlatform
import platform.UIKit.UIViewController

/**
 * iOS entry point, called from ContentView.swift's UIViewControllerRepresentable.
 * Mirrors what SkillXApplication.onCreate() + MainActivity.kt do together on Android.
 */
fun MainViewController(): UIViewController {
    initKoin()
    val navigator = KoinPlatform.getKoin().get<AppNavigator>()

    return ComposeUIViewController {
        SkillXTheme {
            SkillXNavGraph(navigator = navigator)
        }
    }
}
