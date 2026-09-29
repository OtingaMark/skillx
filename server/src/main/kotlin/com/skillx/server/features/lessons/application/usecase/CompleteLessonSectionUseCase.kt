package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.domain.service.LessonAccessPolicy
import com.skillx.server.features.lessons.domain.service.LessonProgressCalculator
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * The teacher confirms the learner finished the current section. Sections complete strictly
 * in order; repeating the call for an already-completed section is a no-op.
 */
class CompleteLessonSectionUseCase(
    private val lessonRepository: LessonRepository,
    private val transactionRunner: FirestoreTransactionRunner
) {
    suspend operator fun invoke(lessonId: String, sectionId: String, teacherId: String): LessonRequest {
        return transactionRunner.runTransaction { tx ->
            val lesson = lessonRepository.getById(lessonId, tx) ?: throw NotFoundException("Lesson not found.")
            LessonAccessPolicy.requireTeacher(lesson, teacherId)
            if (lesson.sections.none { it.id == sectionId }) throw NotFoundException("Section not found.")
            if (sectionId in lesson.completedSectionIds) return@runTransaction lesson
            if (lesson.status != LessonStatus.ACCEPTED) {
                throw ConflictException("INVALID_STATUS", "Sections can only be completed on an accepted lesson.")
            }
            val currentId = LessonProgressCalculator.calculate(lesson).currentSectionId
            if (sectionId != currentId) {
                throw ConflictException("SECTION_OUT_OF_ORDER", "Complete the current section first.")
            }
            val updated = lesson.copy(completedSectionIds = lesson.completedSectionIds + sectionId)
            lessonRepository.update(updated, tx)
            updated
        }
    }
}
