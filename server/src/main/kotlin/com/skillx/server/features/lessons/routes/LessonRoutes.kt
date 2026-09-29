package com.skillx.server.features.lessons.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.core.exceptions.*
import com.skillx.server.features.lessons.application.usecase.AcceptLessonRequestUseCase
import com.skillx.server.features.lessons.application.usecase.CompleteLessonUseCase
import com.skillx.server.features.lessons.application.usecase.CreateLessonRequestUseCase
import com.skillx.server.features.lessons.domain.model.LessonStatus
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class CreateLessonRequest(val teacherId: String, val skill: String)
@Serializable data class LessonResponse(
    val id: String, val requesterId: String, val teacherId: String,
    val requesterName: String, val teacherName: String, val skill: String, val status: String
) {
    companion object {
        fun fromDomain(lesson: com.skillx.server.features.lessons.domain.model.LessonRequest): LessonResponse =
            LessonResponse(lesson.id, lesson.requesterId, lesson.teacherId, lesson.requesterName, lesson.teacherName, lesson.skill, lesson.status.name)
    }
}

/**
 * Lesson HTTP routes.
 * Thin layer that delegates to use cases — no business logic or direct Firestore access here.
 */
fun Route.lessonRoutes() {
    val createUseCase by inject<CreateLessonRequestUseCase>()
    val acceptUseCase by inject<AcceptLessonRequestUseCase>()
    val completeUseCase by inject<CompleteLessonUseCase>()

    route("/lessons") {
        post {
            val uid = call.userId()
            val request = call.receive<CreateLessonRequest>()
            val lesson = createUseCase(uid, request.teacherId, request.skill)
            call.respond(HttpStatusCode.Created, LessonResponse.fromDomain(lesson))
        }

        get {
            val uid = call.userId()
            // For listing lessons, we'd need a GetUserLessonsUseCase
            // For now, return empty list - implement GetUserLessonsUseCase for full functionality
            call.respond(emptyList<LessonResponse>())
        }

        put("/{lessonId}/accept") {
            val uid = call.userId()
            val lessonId = call.parameters["lessonId"]!!
            val lesson = acceptUseCase(lessonId, uid)
            call.respond(LessonResponse.fromDomain(lesson))
        }

        put("/{lessonId}/complete") {
            val uid = call.userId()
            val lessonId = call.parameters["lessonId"]!!
            val lesson = completeUseCase(lessonId, uid)
            call.respond(LessonResponse.fromDomain(lesson))
        }
    }
}