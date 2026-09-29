package com.skillx.server.features.lessons.domain.service

import com.skillx.server.core.exceptions.AuthorizationException
import com.skillx.server.features.lessons.domain.model.LessonRequest

/** Single place deciding who may read or modify a lesson. */
object LessonAccessPolicy {

    fun requireParticipant(lesson: LessonRequest, userId: String) {
        if (userId != lesson.requesterId && userId != lesson.teacherId) {
            throw AuthorizationException("You are not a participant in this lesson.")
        }
    }

    fun requireTeacher(lesson: LessonRequest, userId: String) {
        if (userId != lesson.teacherId) {
            throw AuthorizationException("Only the teacher can change this lesson.")
        }
    }
}
