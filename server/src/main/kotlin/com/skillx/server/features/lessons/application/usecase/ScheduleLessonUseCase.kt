package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.domain.service.LessonAccessPolicy
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/** The teacher sets when the next session happens. Must be in the future, per the server clock. */
class ScheduleLessonUseCase(
    private val lessonRepository: LessonRepository,
    private val transactionRunner: FirestoreTransactionRunner,
    private val now: () -> Long = System::currentTimeMillis
) {
    suspend operator fun invoke(lessonId: String, teacherId: String, scheduledAtEpochMillis: Long): LessonRequest {
        val current = now()
        if (scheduledAtEpochMillis <= current || scheduledAtEpochMillis > current + MAX_AHEAD_MILLIS) {
            throw ValidationException("INVALID_SCHEDULE", "Pick a time in the future, within the next year.")
        }
        return transactionRunner.runTransaction { tx ->
            val lesson = lessonRepository.getById(lessonId, tx) ?: throw NotFoundException("Lesson not found.")
            LessonAccessPolicy.requireTeacher(lesson, teacherId)
            if (lesson.status != LessonStatus.ACCEPTED) {
                throw ConflictException("INVALID_STATUS", "Only accepted lessons can be scheduled.")
            }
            val updated = lesson.copy(scheduledAtEpochMillis = scheduledAtEpochMillis)
            lessonRepository.update(updated, tx)
            updated
        }
    }

    companion object {
        const val MAX_AHEAD_MILLIS = 365L * 24 * 60 * 60 * 1000
    }
}
