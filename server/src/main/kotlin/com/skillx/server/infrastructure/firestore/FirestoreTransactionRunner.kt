package com.skillx.server.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.Transaction
import kotlinx.coroutines.runBlocking

/**
 * Runs Firestore transactions with Kotlin coroutines support.
 */
class FirestoreTransactionRunner(private val provider: FirestoreClientProvider) {

    /**
     * Executes a block within a Firestore transaction.
     * Firestore's transaction callback is a plain (non-suspend) Java callback executed on
     * its own thread, so the suspend block is bridged in with runBlocking rather than awaited.
     * @param block The transaction block to execute
     * @return The result of the transaction block
     */
    suspend fun <T> runTransaction(block: suspend (Transaction) -> T): T {
        val db = provider.getFirestore()
        return db.runTransaction { tx -> runBlocking { block(tx) } }.await()
    }

    /**
     * Executes a block within a Firestore transaction (non-suspend version for backward compatibility).
     * @param block The transaction block to execute
     * @return The result of the transaction block
     */
    fun <T> runInTransaction(block: (Transaction) -> T): T {
        val db = provider.getFirestore()
        return db.runTransaction { tx -> block(tx) }.get()
    }
}