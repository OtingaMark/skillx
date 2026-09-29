package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.repository.LessonRepository

/** Lessons the caller takes part in, as learner or teacher. */
class ListUserLessonsUseCase(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(userId: String): List<LessonRequest> =
        lessonRepository.findByUser(userId)
}
