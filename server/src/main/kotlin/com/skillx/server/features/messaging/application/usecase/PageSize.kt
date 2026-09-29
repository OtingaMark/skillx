package com.skillx.server.features.messaging.application.usecase

import com.skillx.server.features.messaging.domain.model.MessagingLimits

/** Clamps a client-requested page size into the allowed range. */
internal fun resolvePageSize(requested: Int?): Int =
    (requested ?: MessagingLimits.DEFAULT_PAGE_SIZE).coerceIn(1, MessagingLimits.MAX_PAGE_SIZE)
