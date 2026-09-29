package com.skillx.server.features.points.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Transaction
import com.skillx.server.infrastructure.firestore.await
import com.skillx.server.features.points.domain.model.PointTransaction
import com.skillx.server.features.points.domain.model.TransactionReason
import com.skillx.server.features.points.domain.repository.PointLedgerRepository
import com.skillx.server.features.points.domain.repository.PointRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider

/**
 * Firestore implementation of PointRepository and PointLedgerRepository.
 * Handles user point balances and the point transaction ledger.
 */
class FirestorePointDataSource(
    private val provider: FirestoreClientProvider
) : PointRepository, PointLedgerRepository {

    private val db = provider.getFirestore()
    private val usersCollection = db.collection("users")
    private val ledgerCollection = db.collection("pointTransactions")

    // PointRepository implementation

    override suspend fun getBalance(userId: String, tx: Transaction?): Int {
        val doc = if (tx != null) {
            tx.get(usersCollection.document(userId)).await()
        } else {
            usersCollection.document(userId).get().await()
        }
        return if (doc.exists()) (doc.getLong("points") ?: 0L).toInt() else 0
    }

    override suspend fun setBalance(userId: String, balance: Int, tx: Transaction?) {
        val data = mapOf("points" to balance)
        if (tx != null) {
            tx.set(usersCollection.document(userId), data, com.google.cloud.firestore.SetOptions.merge())
        } else {
            usersCollection.document(userId).set(data, com.google.cloud.firestore.SetOptions.merge()).await()
        }
    }

    override suspend fun adjustBalance(userId: String, delta: Int, tx: Transaction?) {
        if (delta == 0) return
        val currentBalance = getBalance(userId, tx)
        val newBalance = currentBalance + delta
        setBalance(userId, newBalance, tx)
    }

    // PointLedgerRepository implementation

    override suspend fun create(transaction: PointTransaction, tx: Transaction?) {
        val data = mapOf(
            "id" to transaction.id,
            "fromUserId" to transaction.fromUserId,
            "toUserId" to transaction.toUserId,
            "amount" to transaction.amount,
            "reason" to transaction.reason.name,
            "lessonId" to transaction.lessonId,
            "timestamp" to transaction.timestamp
        )
        if (tx != null) {
            tx.set(ledgerCollection.document(transaction.id), data)
        } else {
            ledgerCollection.document(transaction.id).set(data).await()
        }
    }

    override suspend fun getHistory(userId: String): List<PointTransaction> {
        val sent = ledgerCollection.whereEqualTo("fromUserId", userId).get().await()
        val received = ledgerCollection.whereEqualTo("toUserId", userId).get().await()
        val allDocs = (sent.documents + received.documents).distinctBy { it.id }
        return allDocs.map { fromFirestore(it) }
            .sortedByDescending { it.timestamp }
    }

    override suspend fun getById(id: String): PointTransaction? {
        val doc = ledgerCollection.document(id).get().await()
        return if (doc.exists()) fromFirestore(doc) else null
    }

    private fun fromFirestore(doc: DocumentSnapshot): PointTransaction {
        return PointTransaction(
            id = doc.id,
            fromUserId = doc.getString("fromUserId") ?: "",
            toUserId = doc.getString("toUserId") ?: "",
            amount = (doc.getLong("amount") ?: 0L).toInt(),
            reason = TransactionReason.valueOf(doc.getString("reason") ?: "LESSON_COMPLETED"),
            lessonId = doc.getString("lessonId"),
            timestamp = (doc.getLong("timestamp") ?: 0L)
        )
    }
}