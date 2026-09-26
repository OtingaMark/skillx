package com.skillx.navigation.route

import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.matching.domain.model.SkillMatch

/**
 * Type-safe sealed class replacing the string-based when(screen) state machine
 * from MainActivity.kt L211-503.
 */
sealed class SkillXRoute {
    data object Welcome : SkillXRoute()
    data object SignUp : SkillXRoute()
    data object Login : SkillXRoute()
    data object Home : SkillXRoute()
    data object Profile : SkillXRoute()
    data object EditProfile : SkillXRoute()
    data object Skills : SkillXRoute()
    data object Matches : SkillXRoute()
    data class MatchProfile(val match: SkillMatch) : SkillXRoute()
    data class RequestLesson(val match: SkillMatch) : SkillXRoute()
    data object LessonRequests : SkillXRoute()
    data class RateLesson(val request: LessonRequest) : SkillXRoute()
    data object Safety : SkillXRoute()
    data object HowItWorks : SkillXRoute()
    data class ReportUser(val match: SkillMatch) : SkillXRoute()
    data object RevenueCat : SkillXRoute()
}
