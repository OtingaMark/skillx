package com.skillx.features.lessons.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.repository.LessonRepository

/**
 * Accepts a pending lesson request.
 * Server validates that the caller is the request's teacher.
 */
class AcceptLessonRequestUseCase(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String): AppResult<LessonRequest, AppError> {
        return lessonRepository.acceptLessonRequest(lessonId)
    }
}
