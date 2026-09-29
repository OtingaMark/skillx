package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.AuthorizationException
import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * Accepts a pending lesson request.
 * Only the teacher (recipient of the request) can accept.
 */
class AcceptLessonRequestUseCase(
    private val lessonRepository: LessonRepository,
    private val transactionRunner: FirestoreTransactionRunner
) {

    /**
     * Accepts a lesson request.
     * @param lessonId The ID of the lesson request to accept
     * @param teacherId The ID of the teacher accepting (must match lesson.teacherId)
     * @return The updated LessonRequest with ACCEPTED status
     * @throws NotFoundException if lesson doesn't exist
     * @throws AuthorizationException if caller is not the teacher
     * @throws ConflictException if lesson is not in PENDING status
     */
    suspend operator fun invoke(lessonId: String, teacherId: String): LessonRequest {
        return transactionRunner.runTransaction { tx ->
            val lesson = lessonRepository.getById(lessonId, tx)
                ?: throw NotFoundException("Lesson request not found.")

            // Authorization: only the teacher can accept
            if (lesson.teacherId != teacherId) {
                throw AuthorizationException("Only the teacher can accept this request.")
            }

            // Validate status
            if (lesson.status != LessonStatus.PENDING) {
                throw ConflictException("INVALID_STATUS", "Can only accept pending requests.")
            }

            val acceptedLesson = lesson.copy(status = LessonStatus.ACCEPTED)
            lessonRepository.update(acceptedLesson, tx)

            acceptedLesson
        }
    }
}