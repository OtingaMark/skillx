package com.skillx.server.features.lessons.domain.service

import com.skillx.server.features.lessons.domain.model.LessonProgress
import com.skillx.server.features.lessons.domain.model.LessonRequest
import com.skillx.server.features.lessons.domain.model.LessonStatus
import com.skillx.server.features.lessons.domain.model.SectionProgress
import com.skillx.server.features.lessons.domain.model.SectionStatus

/**
 * Derives section statuses and percentage from the stored plan and completed section ids.
 * Sections are completed strictly in order, so the first incomplete one is CURRENT and the
 * rest are LOCKED.
 */
object LessonProgressCalculator {

    fun calculate(lesson: LessonRequest): LessonProgress {
        val completed = lesson.completedSectionIds.toSet()
        val currentId = lesson.sections.firstOrNull { it.id !in completed }?.id
        val sections = lesson.sections.map { section ->
            val status = when {
                section.id in completed -> SectionStatus.COMPLETED
                section.id == currentId -> SectionStatus.CURRENT
                else -> SectionStatus.LOCKED
            }
            SectionProgress(section, status)
        }
        val completedCount = sections.count { it.status == SectionStatus.COMPLETED }
        val total = sections.size
        val percent = when {
            lesson.status == LessonStatus.COMPLETED -> 100
            total == 0 -> 0
            else -> completedCount * 100 / total
        }
        return LessonProgress(
            sections = sections,
            completedCount = completedCount,
            totalCount = total,
            percent = percent,
            currentSectionId = if (lesson.status == LessonStatus.COMPLETED) null else currentId
        )
    }
}
