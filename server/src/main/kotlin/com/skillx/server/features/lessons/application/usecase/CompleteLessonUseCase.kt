package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.InsufficientPointsException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.lessons.domain.model.LessonPricing
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.domain.service.LessonAccessPolicy
import com.skillx.server.features.lessons.domain.service.LessonProgressCalculator
import com.skillx.server.features.points.domain.model.TransactionReason
import com.skillx.server.features.points.domain.service.PointTransferService
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * Completes a lesson and transfers points from learner to teacher, atomically:
 * the lesson must be ACCEPTED, the caller a participant, every plan section done (when the
 * lesson has a plan), and the learner able to pay. The amount paid is recorded on the lesson.
 */
class CompleteLessonUseCase(
    private val lessonRepository: LessonRepository,
    private val pointTransferService: PointTransferService,
    private val transactionRunner: FirestoreTransactionRunner,
    private val now: () -> Long = System::currentTimeMillis
) {

    suspend operator fun invoke(lessonId: String, callerId: String): LessonRequest {
        return transactionRunner.runTransaction { tx ->
            val lesson = lessonRepository.getById(lessonId, tx)
                ?: throw NotFoundException("Lesson request not found.")

            LessonAccessPolicy.requireParticipant(lesson, callerId)

            if (lesson.status != LessonStatus.ACCEPTED) {
                throw ConflictException("INVALID_STATUS", "Can only complete accepted lessons.")
            }

            val progress = LessonProgressCalculator.calculate(lesson)
            if (progress.completedCount < progress.totalCount) {
                throw ConflictException("PLAN_INCOMPLETE", "Every section of the lesson plan must be completed first.")
            }

            val transferred = pointTransferService.transferPoints(
                fromUserId = lesson.requesterId,
                toUserId = lesson.teacherId,
                amount = LessonPricing.POINTS_PER_LESSON,
                reason = TransactionReason.LESSON_COMPLETED,
                lessonId = lessonId
            )
            if (!transferred) {
                throw InsufficientPointsException("Learner has insufficient points.")
            }

            val completedLesson = lesson.copy(
                status = LessonStatus.COMPLETED,
                completedAtEpochMillis = now(),
                pointsTransferred = LessonPricing.POINTS_PER_LESSON
            )
            lessonRepository.update(completedLesson, tx)

            completedLesson
        }
    }
}
