package com.skillx.features.lessons.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.repository.LessonRepository

/**
 * Loads all lesson requests for the current user (both as requester and teacher).
 */
class ObserveMyLessonsUseCase(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(): AppResult<List<LessonRequest>, AppError> {
        return lessonRepository.getMyLessonRequests()
    }
}
