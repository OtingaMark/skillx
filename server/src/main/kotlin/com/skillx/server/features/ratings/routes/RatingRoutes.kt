package com.skillx.server.features.ratings.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.core.exceptions.ConflictException
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class SubmitRatingRequest(val lessonId: String, val ratedUserId: String, val rating: Int, val comment: String)
@Serializable data class RatingResponse(val id: String, val lessonId: String, val raterId: String, val ratedUserId: String, val ratedUserName: String, val rating: Int, val comment: String, val timestamp: Long)
@Serializable data class SummaryResponse(val userId: String, val averageRating: Double?, val totalRatings: Int)
@Serializable data class AlreadyRatedResponse(val alreadyRated: Boolean)

fun Route.ratingRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/ratings") {
        post {
            val uid = call.userId()
            val request = call.receive<SubmitRatingRequest>()
            val db = firestoreProvider.getFirestore()
            val existing = db.collection("ratings").whereEqualTo("lessonId", request.lessonId).whereEqualTo("raterId", uid).get().get()
            if (!existing.isEmpty) throw ConflictException("ALREADY_RATED", "You have already rated this lesson.")
            val ratedDoc = db.collection("users").document(request.ratedUserId).get().get()
            val ratingId = java.util.UUID.randomUUID().toString()
            val ts = System.currentTimeMillis()
            val data = mapOf("lessonId" to request.lessonId, "raterId" to uid, "ratedUserId" to request.ratedUserId,
                "ratedUserName" to (ratedDoc.getString("name") ?: ""), "rating" to request.rating, "comment" to request.comment, "timestamp" to ts)
            db.collection("ratings").document(ratingId).set(data).get()
            call.respond(HttpStatusCode.Created, RatingResponse(ratingId, request.lessonId, uid, request.ratedUserId,
                ratedDoc.getString("name") ?: "", request.rating, request.comment, ts))
        }
        get("/summary/{userId}") {
            val userId = call.parameters["userId"]!!
            val docs = firestoreProvider.getFirestore().collection("ratings").whereEqualTo("ratedUserId", userId).get().get()
            val ratings = docs.documents.mapNotNull { (it.getLong("rating") ?: 0).toInt() }
            val avg = if (ratings.isEmpty()) null else ratings.average()
            call.respond(SummaryResponse(userId, avg, ratings.size))
        }
        get("/check/{lessonId}") {
            val uid = call.userId()
            val lessonId = call.parameters["lessonId"]!!
            val exists = !firestoreProvider.getFirestore().collection("ratings").whereEqualTo("lessonId", lessonId).whereEqualTo("raterId", uid).get().get().isEmpty
            call.respond(AlreadyRatedResponse(exists))
        }
    }
}
