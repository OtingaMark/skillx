package com.skillx.server.infrastructure.jobs.job

import com.skillx.server.features.lessons.application.usecase.CompleteLessonUseCase
import com.skillx.server.features.notifications.application.usecase.SendLessonNotificationUseCase
import org.jobrunr.jobs.annotations.Job
import org.jobrunr.jobs.context.JobContext

class ProcessLessonCompletionJob(
    private val completeLessonUseCase: CompleteLessonUseCase,
    private val sendNotificationUseCase: SendLessonNotificationUseCase
) {

    @Job(name = "Process lesson completion: %{lessonId}")
    fun execute(lessonId: String, context: JobContext) {
        // Post-completion processing: update stats, send notifications
    }
}