package com.skillx.server.features.authentication.infrastructure.firestore

import com.skillx.server.features.authentication.domain.repository.PasswordResetTokenRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

/**
 * Firestore implementation of [PasswordResetTokenRepository].
 * `passwordResetTokens/{sha256(token)}` — { userId, active, expiresAtEpochMillis, createdAtEpochMillis, usedAtEpochMillis? }
 */
class FirestorePasswordResetTokenDataSource(
    provider: FirestoreClientProvider,
    private val transactionRunner: FirestoreTransactionRunner
) : PasswordResetTokenRepository {

    private val db = provider.getFirestore()
    private val tokens = db.collection("passwordResetTokens")
    private val users = db.collection("users")

    override suspend fun issue(userId: String, tokenHash: String, expiresAtEpochMillis: Long, nowEpochMillis: Long) {
        transactionRunner.runTransaction { tx ->
            val outstanding = tx.get(activeTokensOf(userId)).get().documents
            outstanding.forEach { tx.update(it.reference, "active", false) }
            tx.set(
                tokens.document(tokenHash),
                mapOf(
                    "userId" to userId,
                    "active" to true,
                    "expiresAtEpochMillis" to expiresAtEpochMillis,
                    "createdAtEpochMillis" to nowEpochMillis
                )
            )
        }
    }

    override suspend fun consume(tokenHash: String, newPasswordHash: String, nowEpochMillis: Long): Boolean =
        transactionRunner.runTransaction { tx ->
            val tokenRef = tokens.document(tokenHash)
            val token = tx.get(tokenRef).get()
            val userId = token.getString("userId")
            val active = token.getBoolean("active") == true
            val expiresAt = token.getLong("expiresAtEpochMillis") ?: 0L
            if (!token.exists() || userId == null || !active || expiresAt <= nowEpochMillis) {
                return@runTransaction false
            }

            // Firestore transactions require every read before the first write.
            val otherOutstanding = tx.get(activeTokensOf(userId)).get().documents
                .filter { it.id != tokenHash }

            tx.update(tokenRef, mapOf("active" to false, "usedAtEpochMillis" to nowEpochMillis))
            otherOutstanding.forEach { tx.update(it.reference, "active", false) }
            tx.update(users.document(userId), "passwordHash", newPasswordHash)
            true
        }

    private fun activeTokensOf(userId: String) =
        tokens.whereEqualTo("userId", userId).whereEqualTo("active", true)
}
