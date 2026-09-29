package com.skillx.server.features.lessons.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.skillx.server.features.lessons.domain.model.LessonMaterial
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonSection
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.model.MaterialType

/**
 * Maps between LessonRequest domain model and Firestore document format.
 * Documents written before the lesson plan existed simply lack the newer fields and map to
 * an empty plan with no progress.
 */
object LessonDocumentMapper {

    fun toFirestore(lesson: LessonRequest): Map<String, Any?> = mapOf(
        "id" to lesson.id,
        "requesterId" to lesson.requesterId,
        "teacherId" to lesson.teacherId,
        "requesterName" to lesson.requesterName,
        "teacherName" to lesson.teacherName,
        "skill" to lesson.skill,
        "status" to lesson.status.name,
        "sections" to lesson.sections.map { mapOf("id" to it.id, "title" to it.title) },
        "completedSectionIds" to lesson.completedSectionIds,
        "materials" to lesson.materials.map {
            mapOf(
                "id" to it.id,
                "title" to it.title,
                "type" to it.type.name,
                "url" to it.url,
                "addedAtEpochMillis" to it.addedAtEpochMillis
            )
        },
        "scheduledAtEpochMillis" to lesson.scheduledAtEpochMillis,
        "completedAtEpochMillis" to lesson.completedAtEpochMillis,
        "pointsTransferred" to lesson.pointsTransferred
    )

    fun fromFirestore(doc: DocumentSnapshot): LessonRequest {
        return LessonRequest(
            id = doc.id,
            requesterId = doc.getString("requesterId") ?: "",
            teacherId = doc.getString("teacherId") ?: "",
            requesterName = doc.getString("requesterName") ?: "",
            teacherName = doc.getString("teacherName") ?: "",
            skill = doc.getString("skill") ?: "",
            status = LessonStatus.fromString(doc.getString("status") ?: LessonStatus.PENDING.name),
            sections = mapList(doc.get("sections")).mapNotNull(::parseSection),
            completedSectionIds = (doc.get("completedSectionIds") as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
            materials = mapList(doc.get("materials")).mapNotNull(::parseMaterial),
            scheduledAtEpochMillis = (doc.get("scheduledAtEpochMillis") as? Number)?.toLong(),
            completedAtEpochMillis = (doc.get("completedAtEpochMillis") as? Number)?.toLong(),
            pointsTransferred = (doc.get("pointsTransferred") as? Number)?.toInt()
        )
    }

    private fun mapList(value: Any?): List<Map<*, *>> =
        (value as? List<*>)?.filterIsInstance<Map<*, *>>() ?: emptyList()

    private fun parseSection(map: Map<*, *>): LessonSection? {
        val id = map["id"] as? String ?: return null
        val title = map["title"] as? String ?: return null
        return LessonSection(id, title)
    }

    private fun parseMaterial(map: Map<*, *>): LessonMaterial? {
        val id = map["id"] as? String ?: return null
        val title = map["title"] as? String ?: return null
        val url = map["url"] as? String ?: return null
        val type = (map["type"] as? String)?.let { name -> MaterialType.entries.firstOrNull { it.name == name } }
            ?: return null
        val addedAt = (map["addedAtEpochMillis"] as? Number)?.toLong() ?: return null
        return LessonMaterial(id, title, type, url, addedAt)
    }
}
