package com.skillx.server.features.lessons.domain.service
import com.skillx.server.features.lessons.domain.model.LessonStatus
class LessonStateTransitionValidator { fun canTransition(from: LessonStatus, to: LessonStatus): Boolean = when (from) { LessonStatus.PENDING -> to == LessonStatus.ACCEPTED; LessonStatus.ACCEPTED -> to == LessonStatus.COMPLETED; LessonStatus.COMPLETED -> false } }
