package com.skillx.server.features.messaging.domain.model

/** Messaging domain constraints — the single source of truth for input and page-size bounds. */
object MessagingLimits {
    const val MAX_MESSAGE_LENGTH = 2000
    const val MAX_SUBJECT_LENGTH = 80
    const val DEFAULT_PAGE_SIZE = 30
    const val MAX_PAGE_SIZE = 100
}
