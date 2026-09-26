package com.skillx.server.features.points.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class BalanceResponse(val userId: String, val points: Int)
@Serializable data class PointTransactionResponse(val id: String, val fromUserId: String, val toUserId: String, val amount: Int, val reason: String, val lessonId: String? = null, val timestamp: Long)

fun Route.pointRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/points") {
        get("/balance") {
            val uid = call.userId()
            val doc = firestoreProvider.getFirestore().collection("users").document(uid).get().get()
            if (!doc.exists()) throw NotFoundException("User not found.")
            call.respond(BalanceResponse(uid, (doc.getLong("points") ?: 0).toInt()))
        }
        get("/history") {
            val uid = call.userId()
            val db = firestoreProvider.getFirestore()
            val from = db.collection("pointTransactions").whereEqualTo("fromUserId", uid).get().get()
            val to = db.collection("pointTransactions").whereEqualTo("toUserId", uid).get().get()
            val all = (from.documents + to.documents).distinctBy { it.id }.map { d ->
                PointTransactionResponse(d.id, d.getString("fromUserId") ?: "", d.getString("toUserId") ?: "",
                    (d.getLong("amount") ?: 0).toInt(), d.getString("reason") ?: "",
                    d.getString("lessonId"), (d.getLong("timestamp") ?: 0))
            }
            call.respond(all)
        }
    }
}
