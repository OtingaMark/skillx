package com.skillx.server.infrastructure.email

import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties

/** Sends email over SMTP (Jakarta Mail / Eclipse Angus) using [SmtpConfig]. */
class SmtpEmailSender(private val config: SmtpConfig) : EmailSender {

    override suspend fun send(message: EmailMessage) {
        val host = checkNotNull(config.host) { "SMTP is not configured: set SMTP_HOST." }
        val from = checkNotNull(config.fromAddress) { "SMTP is not configured: set SMTP_FROM." }

        withContext(Dispatchers.IO) {
            val mime = MimeMessage(session(host)).apply {
                setFrom(InternetAddress(from))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.to, true))
                subject = message.subject
                setText(message.textBody, Charsets.UTF_8.name())
            }
            Transport.send(mime)
        }
    }

    private fun session(host: String): Session {
        val properties = Properties().apply {
            put("mail.smtp.host", host)
            put("mail.smtp.port", config.port.toString())
            put("mail.smtp.connectiontimeout", "10000")
            put("mail.smtp.timeout", "10000")
            put("mail.smtp.writetimeout", "10000")
            when (config.security) {
                SmtpConfig.SmtpSecurity.NONE -> Unit
                SmtpConfig.SmtpSecurity.STARTTLS -> {
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.starttls.required", "true")
                }
                SmtpConfig.SmtpSecurity.SSL -> put("mail.smtp.ssl.enable", "true")
            }
        }
        val username = config.username
        val password = config.password
        return if (username != null && password != null) {
            properties["mail.smtp.auth"] = "true"
            Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(username, password)
            })
        } else {
            Session.getInstance(properties)
        }
    }
}
