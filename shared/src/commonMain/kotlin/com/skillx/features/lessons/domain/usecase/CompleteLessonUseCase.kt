package com.skillx.features.lessons.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.repository.LessonRepository

/**
 * Marks a lesson as completed.
 * This is the single most security-critical operation (Section 5, Row 97):
 * - Server re-verifies: caller is a participant, lesson status is accepted,
 *   learner balance >= 1, all inside one atomic Firestore transaction.
 * - Also writes a PointTransaction ledger entry.
 */
class CompleteLessonUseCase(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String): AppResult<LessonRequest, AppError> {
        return lessonRepository.completeLesson(lessonId)
    }
}
