package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonSection
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.domain.service.LessonAccessPolicy
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner
import java.util.UUID

/**
 * Replaces a lesson's ordered plan. Teacher only, and only before any section has been
 * completed — rewriting the plan afterwards would silently invalidate recorded progress.
 */
class SetLessonPlanUseCase(
    private val lessonRepository: LessonRepository,
    private val transactionRunner: FirestoreTransactionRunner
) {
    suspend operator fun invoke(lessonId: String, teacherId: String, sectionTitles: List<String>): LessonRequest {
        val titles = sectionTitles.map { it.trim() }
        if (titles.isEmpty() || titles.size > MAX_SECTIONS) {
            throw ValidationException("INVALID_PLAN", "A lesson plan needs between 1 and $MAX_SECTIONS sections.")
        }
        if (titles.any { it.isEmpty() || it.length > MAX_TITLE_LENGTH }) {
            throw ValidationException("INVALID_SECTION_TITLE", "Section titles must be 1 to $MAX_TITLE_LENGTH characters.")
        }

        return transactionRunner.runTransaction { tx ->
            val lesson = lessonRepository.getById(lessonId, tx) ?: throw NotFoundException("Lesson not found.")
            LessonAccessPolicy.requireTeacher(lesson, teacherId)
            if (lesson.status == LessonStatus.COMPLETED) {
                throw ConflictException("LESSON_COMPLETED", "A completed lesson's plan can't be changed.")
            }
            if (lesson.completedSectionIds.isNotEmpty()) {
                throw ConflictException("PLAN_IN_PROGRESS", "The plan can't be changed after sections have been completed.")
            }
            val updated = lesson.copy(sections = titles.map { LessonSection(UUID.randomUUID().toString(), it) })
            lessonRepository.update(updated, tx)
            updated
        }
    }

    companion object {
        const val MAX_SECTIONS = 20
        const val MAX_TITLE_LENGTH = 80
    }
}
