package com.skillx.server.infrastructure.email

/** Delivers transactional email. Throws if delivery fails. */
interface EmailSender {
    suspend fun send(message: EmailMessage)
}
