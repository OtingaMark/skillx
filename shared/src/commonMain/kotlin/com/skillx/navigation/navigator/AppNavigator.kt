package com.skillx.navigation.navigator

import com.skillx.navigation.route.SkillXRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Central navigation state holder.
 * Manages the back stack and current screen state.
 * Replaces the manual `var screen by remember { mutableStateOf("welcome") }` pattern.
 */
class AppNavigator {

    private val _currentRoute = MutableStateFlow<SkillXRoute>(SkillXRoute.Welcome)
    val currentRoute: StateFlow<SkillXRoute> = _currentRoute.asStateFlow()

    private val backStack = mutableListOf<SkillXRoute>()

    fun navigateTo(route: SkillXRoute) {
        backStack.add(_currentRoute.value)
        _currentRoute.value = route
    }

    fun goBack(): Boolean {
        if (backStack.isEmpty()) return false
        _currentRoute.value = backStack.removeAt(backStack.lastIndex)
        return true
    }

    /** Swaps the current screen without growing the back stack (e.g. Sign Up ⇄ Login). */
    fun replaceCurrent(route: SkillXRoute) {
        _currentRoute.value = route
    }

    fun navigateAndClearStack(route: SkillXRoute) {
        backStack.clear()
        _currentRoute.value = route
    }
}
