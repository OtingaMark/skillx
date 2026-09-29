package com.skillx.features.lessons.data.remote

import com.skillx.features.lessons.data.dto.CreateLessonRequestDto
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class LessonApi(private val client: HttpClient) {

    suspend fun createLessonRequest(request: CreateLessonRequestDto): HttpResponse {
        return client.post("/api/v1/lessons") { setBody(request) }
    }

    suspend fun getMyLessonRequests(): HttpResponse {
        return client.get("/api/v1/lessons")
    }

    suspend fun acceptLessonRequest(lessonId: String): HttpResponse {
        return client.put("/api/v1/lessons/$lessonId/accept")
    }

    suspend fun completeLesson(lessonId: String): HttpResponse {
        return client.put("/api/v1/lessons/$lessonId/complete")
    }
}
