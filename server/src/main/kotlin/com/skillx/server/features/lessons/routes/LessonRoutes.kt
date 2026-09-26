package com.skillx.server.features.lessons.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.core.exceptions.*
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class CreateLessonRequest(val teacherId: String, val skill: String)
@Serializable data class LessonResponse(val id: String, val requesterId: String, val teacherId: String, val requesterName: String, val teacherName: String, val skill: String, val status: String)

fun Route.lessonRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/lessons") {
        post {
            val uid = call.userId()
            val request = call.receive<CreateLessonRequest>()
            val db = firestoreProvider.getFirestore()

            // Duplicate check
            val existing = db.collection("lessonRequests").whereEqualTo("requesterId", uid).whereEqualTo("teacherId", request.teacherId).whereEqualTo("skill", request.skill).get().get()
            val hasActive = existing.documents.any { d -> val s = d.getString("status"); s == "pending" || s == "accepted" }
            if (hasActive) throw ConflictException("DUPLICATE_LESSON_REQUEST", "You already have an active lesson request for this skill.")

            val requesterDoc = db.collection("users").document(uid).get().get()
            val teacherDoc = db.collection("users").document(request.teacherId).get().get()
            if (!teacherDoc.exists()) throw NotFoundException("Teacher not found.")

            val lessonId = java.util.UUID.randomUUID().toString()
            val lessonData = mapOf("requesterId" to uid, "teacherId" to request.teacherId,
                "requesterName" to (requesterDoc.getString("name") ?: ""), "teacherName" to (teacherDoc.getString("name") ?: ""),
                "skill" to request.skill, "status" to "pending")
            db.collection("lessonRequests").document(lessonId).set(lessonData).get()

            call.respond(HttpStatusCode.Created, LessonResponse(lessonId, uid, request.teacherId,
                requesterDoc.getString("name") ?: "", teacherDoc.getString("name") ?: "", request.skill, "pending"))
        }

        get {
            val uid = call.userId()
            val db = firestoreProvider.getFirestore()
            val asRequester = db.collection("lessonRequests").whereEqualTo("requesterId", uid).get().get()
            val asTeacher = db.collection("lessonRequests").whereEqualTo("teacherId", uid).get().get()
            val allDocs = (asRequester.documents + asTeacher.documents).distinctBy { it.id }
            val lessons = allDocs.map { d -> LessonResponse(d.id, d.getString("requesterId") ?: "", d.getString("teacherId") ?: "",
                d.getString("requesterName") ?: "", d.getString("teacherName") ?: "", d.getString("skill") ?: "", d.getString("status") ?: "pending") }
            call.respond(lessons)
        }

        put("/{lessonId}/accept") {
            val uid = call.userId()
            val lessonId = call.parameters["lessonId"]!!
            val db = firestoreProvider.getFirestore()
            val doc = db.collection("lessonRequests").document(lessonId).get().get()
            if (!doc.exists()) throw NotFoundException("Lesson request not found.")
            if (doc.getString("teacherId") != uid) throw AuthorizationException("Only the teacher can accept this request.")
            if (doc.getString("status") != "pending") throw ConflictException("INVALID_STATUS", "Can only accept pending requests.")
            db.collection("lessonRequests").document(lessonId).update("status", "accepted").get()
            call.respond(LessonResponse(lessonId, doc.getString("requesterId") ?: "", uid,
                doc.getString("requesterName") ?: "", doc.getString("teacherName") ?: "", doc.getString("skill") ?: "", "accepted"))
        }

        put("/{lessonId}/complete") {
            val uid = call.userId()
            val lessonId = call.parameters["lessonId"]!!
            val db = firestoreProvider.getFirestore()

            // Atomic transaction: validate, deduct, credit, update status, write ledger
            db.runTransaction { tx ->
                val docRef = db.collection("lessonRequests").document(lessonId)
                val doc = tx.get(docRef).get()
                if (!doc.exists()) throw NotFoundException("Lesson request not found.")
                val requesterId = doc.getString("requesterId") ?: throw NotFoundException("Invalid lesson.")
                val teacherId = doc.getString("teacherId") ?: throw NotFoundException("Invalid lesson.")
                if (uid != requesterId && uid != teacherId) throw AuthorizationException("Only participants can complete this lesson.")
                if (doc.getString("status") != "accepted") throw ConflictException("LESSON_ALREADY_COMPLETED", "Can only complete accepted lessons.")

                val requesterRef = db.collection("users").document(requesterId)
                val teacherRef = db.collection("users").document(teacherId)
                val requesterDoc = tx.get(requesterRef).get()
                val teacherDoc = tx.get(teacherRef).get()
                val requesterPoints = (requesterDoc.getLong("points") ?: 0).toInt()
                if (requesterPoints < 1) throw InsufficientPointsException("Learner does not have enough points.")

                tx.update(requesterRef, "points", requesterPoints - 1)
                tx.update(teacherRef, "points", ((teacherDoc.getLong("points") ?: 0).toInt()) + 1)
                tx.update(docRef, "status", "completed")

                // Write ledger entry
                val txId = java.util.UUID.randomUUID().toString()
                val ledger = mapOf("fromUserId" to requesterId, "toUserId" to teacherId, "amount" to 1,
                    "reason" to "LESSON_COMPLETED", "lessonId" to lessonId, "timestamp" to System.currentTimeMillis())
                tx.set(db.collection("pointTransactions").document(txId), ledger)
            }.get()

            val updatedDoc = db.collection("lessonRequests").document(lessonId).get().get()
            call.respond(LessonResponse(lessonId, updatedDoc.getString("requesterId") ?: "", updatedDoc.getString("teacherId") ?: "",
                updatedDoc.getString("requesterName") ?: "", updatedDoc.getString("teacherName") ?: "",
                updatedDoc.getString("skill") ?: "", "completed"))
        }
    }
}
