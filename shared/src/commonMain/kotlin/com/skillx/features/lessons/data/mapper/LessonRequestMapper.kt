package com.skillx.features.lessons.data.mapper
import com.skillx.features.lessons.data.dto.LessonRequestResponseDto
import com.skillx.features.lessons.domain.model.LessonRequest
import com.skillx.features.lessons.domain.model.LessonStatus
object LessonRequestMapper {
    fun toDomain(dto: LessonRequestResponseDto) = LessonRequest(id = dto.id, requesterId = dto.requesterId, teacherId = dto.teacherId, requesterName = dto.requesterName, teacherName = dto.teacherName, skill = dto.skill, status = LessonStatus.fromString(dto.status))
}
