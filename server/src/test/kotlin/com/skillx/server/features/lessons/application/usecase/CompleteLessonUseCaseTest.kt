package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.service.LessonStateTransitionValidator
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CompleteLessonUseCaseTest {
    @Test fun canTransitionFromAcceptedToCompleted() {
        val validator = LessonStateTransitionValidator()
        assertTrue(validator.canTransition(LessonStatus.ACCEPTED, LessonStatus.COMPLETED))
    }
    @Test fun cannotTransitionFromPendingToCompleted() {
        val validator = LessonStateTransitionValidator()
        assertFalse(validator.canTransition(LessonStatus.PENDING, LessonStatus.COMPLETED))
    }
    @Test fun canTransitionFromPendingToAccepted() {
        val validator = LessonStateTransitionValidator()
        assertTrue(validator.canTransition(LessonStatus.PENDING, LessonStatus.ACCEPTED))
    }
}
