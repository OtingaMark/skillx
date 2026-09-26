package com.skillx.server.features.users.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class UserProfileResponse(val id: String, val name: String, val email: String, val teachSkills: List<String>, val learnSkills: List<String>, val points: Int)
@Serializable data class UpdateProfileRequest(val name: String, val teachSkills: List<String>, val learnSkills: List<String>)

fun Route.userRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/users") {
        get("/me") {
            val uid = call.userId()
            val doc = firestoreProvider.getFirestore().collection("users").document(uid).get().get()
            if (!doc.exists()) throw NotFoundException("User not found.")
            @Suppress("UNCHECKED_CAST")
            call.respond(UserProfileResponse(uid, doc.getString("name") ?: "", doc.getString("email") ?: "",
                (doc.get("teachSkills") as? List<String>) ?: emptyList(), (doc.get("learnSkills") as? List<String>) ?: emptyList(),
                (doc.getLong("points") ?: 0).toInt()))
        }
        get("/{userId}") {
            val userId = call.parameters["userId"]!!
            val doc = firestoreProvider.getFirestore().collection("users").document(userId).get().get()
            if (!doc.exists()) throw NotFoundException("User not found.")
            @Suppress("UNCHECKED_CAST")
            call.respond(UserProfileResponse(userId, doc.getString("name") ?: "", doc.getString("email") ?: "",
                (doc.get("teachSkills") as? List<String>) ?: emptyList(), (doc.get("learnSkills") as? List<String>) ?: emptyList(),
                (doc.getLong("points") ?: 0).toInt()))
        }
        put("/me") {
            val uid = call.userId()
            val request = call.receive<UpdateProfileRequest>()
            val updates = mapOf<String, Any>("name" to request.name.trim(), "teachSkills" to request.teachSkills, "learnSkills" to request.learnSkills)
            firestoreProvider.getFirestore().collection("users").document(uid).update(updates).get()
            val doc = firestoreProvider.getFirestore().collection("users").document(uid).get().get()
            @Suppress("UNCHECKED_CAST")
            call.respond(UserProfileResponse(uid, doc.getString("name") ?: "", doc.getString("email") ?: "",
                (doc.get("teachSkills") as? List<String>) ?: emptyList(), (doc.get("learnSkills") as? List<String>) ?: emptyList(),
                (doc.getLong("points") ?: 0).toInt()))
        }
    }
}
