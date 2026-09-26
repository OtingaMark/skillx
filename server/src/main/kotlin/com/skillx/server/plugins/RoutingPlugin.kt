package com.skillx.server.plugins

import com.skillx.server.features.authentication.routes.authRoutes
import com.skillx.server.features.lessons.routes.lessonRoutes
import com.skillx.server.features.matching.routes.matchRoutes
import com.skillx.server.features.payments.routes.paymentRoutes
import com.skillx.server.features.points.routes.pointRoutes
import com.skillx.server.features.ratings.routes.ratingRoutes
import com.skillx.server.features.reports.routes.reportRoutes
import com.skillx.server.features.skills.routes.skillRoutes
import com.skillx.server.features.users.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
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
            }
        }
    }
}
