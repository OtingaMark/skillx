package com.skillx.features.lessons.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.model.LessonRequest

/**
 * Lesson repository interface.
 * All operations go through server API — Firestore access is server-side only.
 */
interface LessonRepository {
    suspend fun createLessonRequest(
        teacherId: String,
        skill: String
    ): AppResult<LessonRequest, AppError>

    suspend fun getMyLessonRequests(): AppResult<List<LessonRequest>, AppError>

    suspend fun acceptLessonRequest(lessonId: String): AppResult<LessonRequest, AppError>

    suspend fun completeLesson(lessonId: String): AppResult<LessonRequest, AppError>
}
