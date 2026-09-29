package com.skillx.server.features.lessons.domain.model

/** Server-computed progress through a lesson plan — never supplied by the client. */
data class LessonProgress(
    val sections: List<SectionProgress>,
    val completedCount: Int,
    val totalCount: Int,
    val percent: Int,
    val currentSectionId: String?
)

data class SectionProgress(
    val section: LessonSection,
    val status: SectionStatus
)
