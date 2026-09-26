package com.skillx.server.features.notifications.application.usecase
class SendLessonNotificationUseCase { suspend operator fun invoke(userId: String, lessonId: String, type: String) = Unit }
