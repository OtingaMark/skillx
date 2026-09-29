package com.skillx.server.features.lessons.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Query
import com.google.cloud.firestore.Transaction
import com.skillx.server.infrastructure.firestore.await
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider

/**
 * Firestore implementation of LessonRepository.
 * Handles all lesson request persistence operations.
 */
class FirestoreLessonDataSource(
    private val provider: FirestoreClientProvider
) : LessonRepository {

    private val db = provider.getFirestore()
    private val collection = db.collection("lessonRequests")

    override suspend fun create(lesson: LessonRequest, tx: Transaction?): String {
        val data = LessonDocumentMapper.toFirestore(lesson)
        if (tx != null) {
            tx.set(collection.document(lesson.id), data)
        } else {
            collection.document(lesson.id).set(data).await()
        }
        return lesson.id
    }

    override suspend fun getById(id: String, tx: Transaction?): LessonRequest? {
        val doc = if (tx != null) {
            tx.get(collection.document(id)).await()
        } else {
            collection.document(id).get().await()
        }
        return if (doc.exists()) LessonDocumentMapper.fromFirestore(doc) else null
    }

    override suspend fun findByUser(userId: String): List<LessonRequest> {
        val asRequester = collection.whereEqualTo("requesterId", userId).get().await()
        val asTeacher = collection.whereEqualTo("teacherId", userId).get().await()
        val allDocs = (asRequester.documents + asTeacher.documents).distinctBy { it.id }
        return allDocs.map { LessonDocumentMapper.fromFirestore(it) }
    }

    override suspend fun findActiveRequest(requesterId: String, teacherId: String, skill: String): LessonRequest? {
        val query = collection
            .whereEqualTo("requesterId", requesterId)
            .whereEqualTo("teacherId", teacherId)
            .whereEqualTo("skill", skill)
            .whereIn("status", listOf(LessonStatus.PENDING.name, LessonStatus.ACCEPTED.name))
            .limit(1)
            .get()
            .await()
        return query.documents.firstOrNull()?.let { LessonDocumentMapper.fromFirestore(it) }
    }

    override suspend fun update(lesson: LessonRequest, tx: Transaction?) {
        val data = LessonDocumentMapper.toFirestore(lesson)
        if (tx != null) {
            tx.set(collection.document(lesson.id), data)
        } else {
            collection.document(lesson.id).set(data).await()
        }
    }

    override suspend fun updateStatus(id: String, status: LessonStatus, tx: Transaction?) {
        val data = mapOf("status" to status.name)
        if (tx != null) {
            tx.update(collection.document(id), data)
        } else {
            collection.document(id).update(data).await()
        }
    }
}