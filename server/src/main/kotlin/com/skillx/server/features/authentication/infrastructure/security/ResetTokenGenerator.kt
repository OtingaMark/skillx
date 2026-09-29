package com.skillx.server.features.authentication.infrastructure.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/** Generates unguessable reset tokens and the SHA-256 hash stored in their place. */
class ResetTokenGenerator(private val random: SecureRandom = SecureRandom()) {

    fun generate(): String {
        val bytes = ByteArray(32)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }

    fun hash(token: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(token.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
}
