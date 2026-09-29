package com.skillx.server.features.onboarding.interfaces.http

import com.skillx.server.core.extensions.userId
import com.skillx.server.features.onboarding.application.usecase.CompleteOnboardingUseCase
import com.skillx.server.features.onboarding.application.usecase.LoadOnboardingProgressUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveAvailabilityUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveGoalsUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveLearnSkillsUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveProficiencyUseCase
import com.skillx.server.features.onboarding.application.usecase.SaveTeachSkillsUseCase
import com.skillx.server.features.onboarding.interfaces.http.response.OnboardingProgressResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Onboarding HTTP routes.
 * Thin layer that delegates to use cases — no business logic here.
 */
fun Route.onboardingRoutes(
    loadProgress: LoadOnboardingProgressUseCase,
    saveTeachSkills: SaveTeachSkillsUseCase,
    saveLearnSkills: SaveLearnSkillsUseCase,
    saveProficiency: SaveProficiencyUseCase,
    saveGoals: SaveGoalsUseCase,
    saveAvailability: SaveAvailabilityUseCase,
    completeOnboarding: CompleteOnboardingUseCase
) {
    route("/onboarding") {
        get("/progress") {
                val userId = call.userId()
                val progress = loadProgress(userId)
                call.respond(OnboardingProgressResponse.fromDomain(progress))
            }

            post("/skills/teach") {
                val userId = call.userId()
                val request = call.receive<com.skillx.server.features.onboarding.interfaces.http.request.SaveTeachSkillsRequest>()
                val progress = saveTeachSkills(userId, request.toEntries())
                call.respond(OnboardingProgressResponse.fromDomain(progress))
            }

            post("/skills/learn") {
                val userId = call.userId()
                val request = call.receive<com.skillx.server.features.onboarding.interfaces.http.request.SaveLearnSkillsRequest>()
                val progress = saveLearnSkills(userId, request.toEntries())
                call.respond(OnboardingProgressResponse.fromDomain(progress))
            }

            post("/proficiency") {
                val userId = call.userId()
                val request = call.receive<com.skillx.server.features.onboarding.interfaces.http.request.SaveProficiencyRequest>()
                val progress = saveProficiency(userId, request.toEntries())
                call.respond(OnboardingProgressResponse.fromDomain(progress))
            }

            post("/goals") {
                val userId = call.userId()
                val request = call.receive<com.skillx.server.features.onboarding.interfaces.http.request.SaveGoalsRequest>()
                val progress = saveGoals(userId, request.learningGoalsSet(), request.teachingGoalsSet())
                call.respond(OnboardingProgressResponse.fromDomain(progress))
            }

            post("/availability") {
                val userId = call.userId()
                val request = call.receive<com.skillx.server.features.onboarding.interfaces.http.request.SaveAvailabilityRequest>()
                val progress = saveAvailability(
                    userId,
                    request.daysSet(),
                    request.timesOfDaySet(),
                    request.lessonFormatsSet(),
                    request.preferredDurationMinutes,
                    request.languagesList()
                )
                call.respond(OnboardingProgressResponse.fromDomain(progress))
            }

        post("/complete") {
            val userId = call.userId()
            val progress = completeOnboarding(userId)
            call.respond(HttpStatusCode.OK, OnboardingProgressResponse.fromDomain(progress))
        }
    }
}