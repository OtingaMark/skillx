package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * Creates a new lesson request from a learner to a teacher.
 * Performs duplicate request check server-side (authoritative).
 */
class CreateLessonRequestUseCase(
    private val lessonRepository: LessonRepository,
    private val userRepository: UserRepository,
    private val transactionRunner: FirestoreTransactionRunner
) {

    /**
     * Creates a lesson request.
     * @param requesterId The ID of the user requesting the lesson
     * @param teacherId The ID of the teacher
     * @param skill The skill to learn
     * @return The created LessonRequest
     * @throws NotFoundException if teacher doesn't exist
     * @throws ConflictException if an active request already exists for this skill
     */
    suspend operator fun invoke(requesterId: String, teacherId: String, skill: String): LessonRequest {
        val trimmedSkill = skill.trim()

        // Verify teacher exists
        val teacher = userRepository.findById(teacherId)
            ?: throw NotFoundException("Teacher not found.")

        // Verify requester exists
        val requester = userRepository.findById(requesterId)
            ?: throw NotFoundException("Requester not found.")

        // Check for duplicate active request (server-authoritative)
        val existing = lessonRepository.findActiveRequest(requesterId, teacherId, trimmedSkill)
        if (existing != null) {
            throw ConflictException("DUPLICATE_LESSON_REQUEST", "You already have an active lesson request for this skill.")
        }

        val lessonId = java.util.UUID.randomUUID().toString()
        val lesson = LessonRequest(
            id = lessonId,
            requesterId = requesterId,
            teacherId = teacherId,
            requesterName = requester.name,
            teacherName = teacher.name,
            skill = trimmedSkill,
            status = LessonStatus.PENDING
        )

        transactionRunner.runTransaction { tx ->
            lessonRepository.create(lesson, tx)
        }

        return lesson
    }
}