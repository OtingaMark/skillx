package com.skillx.server.infrastructure.firestore
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Transaction

class FirestoreTransactionRunner(private val provider: FirestoreClientProvider) {
    fun <T> runInTransaction(block: (Transaction, Firestore) -> T): T {
        val db = provider.getFirestore()
        return db.runTransaction { tx -> block(tx, db) }.get()
    }
}
