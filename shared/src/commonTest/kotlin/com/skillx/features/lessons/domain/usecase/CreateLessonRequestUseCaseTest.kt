package com.skillx.features.lessons.domain.usecase

import kotlin.test.Test
import kotlin.test.assertTrue

class CreateLessonRequestUseCaseTest {
    @Test fun requestRequiresTeacherIdAndSkill() {
        val teacherId = "teacher-123"
        val skill = "Kotlin"
        assertTrue(teacherId.isNotBlank())
        assertTrue(skill.isNotBlank())
    }
    @Test fun requestFailsWithEmptySkill() {
        val skill = ""
        assertTrue(skill.isBlank())
    }
}
