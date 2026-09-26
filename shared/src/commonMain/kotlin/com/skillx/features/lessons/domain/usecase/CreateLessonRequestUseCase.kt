package com.skillx.features.lessons.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.repository.LessonRepository

/**
 * Creates a lesson request from the current user to a teacher.
 * Duplicate-request check is server-authoritative (Section 6 of architecture doc).
 */
class CreateLessonRequestUseCase(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(
        teacherId: String,
        skill: String
    ): AppResult<LessonRequest, AppError> {
        return lessonRepository.createLessonRequest(
            teacherId = teacherId,
            skill = skill.trim()
        )
    }
}
