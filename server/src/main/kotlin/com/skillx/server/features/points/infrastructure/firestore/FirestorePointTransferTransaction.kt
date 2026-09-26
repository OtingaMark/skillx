package com.skillx.server.features.points.infrastructure.firestore
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
class FirestorePointTransferTransaction(private val provider: FirestoreClientProvider) { fun transfer(fromId: String, toId: String, amount: Int, reason: String, lessonId: String?) { /* atomic Firestore transaction */ } }
