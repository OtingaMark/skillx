package com.skillx.server.features.lessons.domain.repository

import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus

/**
 * Repository interface for lesson request operations.
 * All methods that modify data accept a Firestore Transaction for atomicity.
 */
interface LessonRepository {

    /**
     * Creates a new lesson request.
     */
    suspend fun create(lesson: LessonRequest, tx: com.google.cloud.firestore.Transaction? = null): String

    /**
     * Finds a lesson request by ID.
     */
    suspend fun getById(id: String, tx: com.google.cloud.firestore.Transaction? = null): LessonRequest?

    /**
     * Finds all lesson requests involving a user (as requester or teacher).
     */
    suspend fun findByUser(userId: String): List<LessonRequest>

    /**
     * Finds an active (PENDING or ACCEPTED) lesson request between two users for a specific skill.
     * Used for duplicate request prevention.
     */
    suspend fun findActiveRequest(requesterId: String, teacherId: String, skill: String): LessonRequest?

    /**
     * Updates a lesson request (e.g., status change).
     */
    suspend fun update(lesson: LessonRequest, tx: com.google.cloud.firestore.Transaction? = null)

    /**
     * Updates only the status of a lesson request.
     */
    suspend fun updateStatus(id: String, status: LessonStatus, tx: com.google.cloud.firestore.Transaction? = null)
}