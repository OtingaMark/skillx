package com.skillx.features.lessons.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class LessonStatusTest {
    @Test fun fromStringPending() = assertEquals(LessonStatus.PENDING, LessonStatus.fromString("pending"))
    @Test fun fromStringAccepted() = assertEquals(LessonStatus.ACCEPTED, LessonStatus.fromString("accepted"))
    @Test fun fromStringCompleted() = assertEquals(LessonStatus.COMPLETED, LessonStatus.fromString("completed"))
    @Test fun fromStringUnknown() = assertEquals(LessonStatus.PENDING, LessonStatus.fromString("unknown"))
    @Test fun asString() = assertEquals("pending", LessonStatus.PENDING.asString())
}
