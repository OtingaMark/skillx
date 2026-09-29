package com.skillx.server.infrastructure.email

/** A plain-text transactional email. */
data class EmailMessage(
    val to: String,
    val subject: String,
    val textBody: String
)
