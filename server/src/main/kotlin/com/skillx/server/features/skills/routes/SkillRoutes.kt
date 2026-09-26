package com.skillx.server.features.skills.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class SkillsResponse(val teachSkills: List<String>, val learnSkills: List<String>)
@Serializable data class SaveSkillsRequest(val teachSkills: List<String>, val learnSkills: List<String>)

fun Route.skillRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/skills") {
        get {
            val uid = call.userId()
            val doc = firestoreProvider.getFirestore().collection("users").document(uid).get().get()
            @Suppress("UNCHECKED_CAST")
            call.respond(SkillsResponse((doc.get("teachSkills") as? List<String>) ?: emptyList(), (doc.get("learnSkills") as? List<String>) ?: emptyList()))
        }
        put {
            val uid = call.userId()
            val request = call.receive<SaveSkillsRequest>()
            firestoreProvider.getFirestore().collection("users").document(uid).update(mapOf<String, Any>("teachSkills" to request.teachSkills, "learnSkills" to request.learnSkills)).get()
            call.respond(SkillsResponse(request.teachSkills, request.learnSkills))
        }
    }
}
