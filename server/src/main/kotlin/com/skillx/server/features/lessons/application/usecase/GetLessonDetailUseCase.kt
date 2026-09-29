package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.domain.service.LessonAccessPolicy

/** Loads one lesson; only its learner and teacher may read it. */
class GetLessonDetailUseCase(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String, callerId: String): LessonRequest {
        val lesson = lessonRepository.getById(lessonId) ?: throw NotFoundException("Lesson not found.")
        LessonAccessPolicy.requireParticipant(lesson, callerId)
        return lesson
    }
}
