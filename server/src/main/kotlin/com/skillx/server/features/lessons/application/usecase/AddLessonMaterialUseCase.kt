package com.skillx.server.features.lessons.application.usecase

import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.lessons.domain.model.LessonMaterial
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.model.MaterialType
import com.skillx.server.features.lessons.domain.repository.LessonRepository
import com.skillx.server.features.lessons.domain.service.LessonAccessPolicy
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner
import java.net.URI
import java.util.UUID

/**
 * The teacher attaches a resource by HTTPS link. The server only stores the link — it never
 * fetches it — so a malicious URL can't be used to make the server call internal hosts.
 */
class AddLessonMaterialUseCase(
    private val lessonRepository: LessonRepository,
    private val transactionRunner: FirestoreTransactionRunner,
    private val now: () -> Long = System::currentTimeMillis
) {
    suspend operator fun invoke(lessonId: String, teacherId: String, title: String, type: String, url: String): LessonRequest {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty() || cleanTitle.length > MAX_TITLE_LENGTH) {
            throw ValidationException("INVALID_MATERIAL_TITLE", "Material titles must be 1 to $MAX_TITLE_LENGTH characters.")
        }
        val materialType = MaterialType.entries.firstOrNull { it.name == type }
            ?: throw ValidationException("INVALID_MATERIAL_TYPE", "Unknown material type.")
        val cleanUrl = url.trim()
        if (!isHttpsUrl(cleanUrl)) {
            throw ValidationException("INVALID_MATERIAL_URL", "Materials must be a valid https:// link.")
        }

        return transactionRunner.runTransaction { tx ->
            val lesson = lessonRepository.getById(lessonId, tx) ?: throw NotFoundException("Lesson not found.")
            LessonAccessPolicy.requireTeacher(lesson, teacherId)
            if (lesson.status == LessonStatus.COMPLETED) {
                throw ConflictException("LESSON_COMPLETED", "Materials can't be added to a completed lesson.")
            }
            if (lesson.materials.size >= MAX_MATERIALS) {
                throw ConflictException("TOO_MANY_MATERIALS", "A lesson can have at most $MAX_MATERIALS materials.")
            }
            val material = LessonMaterial(UUID.randomUUID().toString(), cleanTitle, materialType, cleanUrl, now())
            val updated = lesson.copy(materials = lesson.materials + material)
            lessonRepository.update(updated, tx)
            updated
        }
    }

    private fun isHttpsUrl(value: String): Boolean {
        if (value.length > MAX_URL_LENGTH) return false
        val uri = runCatching { URI(value) }.getOrNull() ?: return false
        return uri.scheme.equals("https", ignoreCase = true) && !uri.host.isNullOrBlank()
    }

    companion object {
        const val MAX_MATERIALS = 30
        const val MAX_TITLE_LENGTH = 120
        const val MAX_URL_LENGTH = 2048
    }
}
