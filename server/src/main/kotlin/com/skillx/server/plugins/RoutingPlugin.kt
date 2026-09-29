package com.skillx.server.plugins

import com.skillx.server.features.authentication.routes.authRoutes
import com.skillx.server.features.lessons.routes.lessonRoutes
import com.skillx.server.features.matching.routes.matchRoutes
import com.skillx.server.features.onboarding.interfaces.http.onboardingRoutes
import com.skillx.server.features.payments.routes.paymentRoutes
import com.skillx.server.features.points.routes.pointRoutes
import com.skillx.server.features.ratings.routes.ratingRoutes
import com.skillx.server.features.reports.routes.reportRoutes
import com.skillx.server.features.skills.routes.skillRoutes
import com.skillx.server.features.users.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val loadProgress by inject<com.skillx.server.features.onboarding.application.usecase.LoadOnboardingProgressUseCase>()
    val saveTeachSkills by inject<com.skillx.server.features.onboarding.application.usecase.SaveTeachSkillsUseCase>()
    val saveLearnSkills by inject<com.skillx.server.features.onboarding.application.usecase.SaveLearnSkillsUseCase>()
    val saveProficiency by inject<com.skillx.server.features.onboarding.application.usecase.SaveProficiencyUseCase>()
    val saveGoals by inject<com.skillx.server.features.onboarding.application.usecase.SaveGoalsUseCase>()
    val saveAvailability by inject<com.skillx.server.features.onboarding.application.usecase.SaveAvailabilityUseCase>()
    val completeOnboarding by inject<com.skillx.server.features.onboarding.application.usecase.CompleteOnboardingUseCase>()

    routing {
        route("/api/v1") {
            authRoutes()
            authenticate("auth-jwt") {
                userRoutes()
                skillRoutes()
                matchRoutes()
                lessonRoutes()
                pointRoutes()
                ratingRoutes()
                reportRoutes()
                paymentRoutes()
                onboardingRoutes(
                    loadProgress = loadProgress,
                    saveTeachSkills = saveTeachSkills,
                    saveLearnSkills = saveLearnSkills,
                    saveProficiency = saveProficiency,
                    saveGoals = saveGoals,
                    saveAvailability = saveAvailability,
                    completeOnboarding = completeOnboarding
                )
            }
        }
    }
}
