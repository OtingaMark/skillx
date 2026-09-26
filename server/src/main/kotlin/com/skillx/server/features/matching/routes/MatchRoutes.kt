package com.skillx.server.features.matching.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class MatchResponse(val uid: String, val name: String, val email: String, val teachSkills: List<String>, val learnSkills: List<String>, val matchedSkill: String)

fun Route.matchRoutes() {
    val firestoreProvider by inject<FirestoreClientProvider>()
    route("/matches") {
        get {
            val uid = call.userId()
            val db = firestoreProvider.getFirestore()
            val myDoc = db.collection("users").document(uid).get().get()
            @Suppress("UNCHECKED_CAST")
            val myLearnSkills = (myDoc.get("learnSkills") as? List<String>) ?: emptyList()

            if (myLearnSkills.isEmpty()) { call.respond(emptyList<MatchResponse>()); return@get }

            val allUsers = db.collection("users").get().get().documents
            val matches = mutableListOf<MatchResponse>()

            for (doc in allUsers) {
                if (doc.id == uid) continue
                @Suppress("UNCHECKED_CAST")
                val theirTeachSkills = (doc.get("teachSkills") as? List<String>) ?: emptyList()
                val matchedSkill = myLearnSkills.firstOrNull { mine -> theirTeachSkills.any { theirs -> mine.trim().equals(theirs.trim(), ignoreCase = true) } }
                if (matchedSkill != null) {
                    @Suppress("UNCHECKED_CAST")
                    matches.add(MatchResponse(doc.id, doc.getString("name") ?: "", doc.getString("email") ?: "",
                        theirTeachSkills, (doc.get("learnSkills") as? List<String>) ?: emptyList(), matchedSkill))
                }
            }
            call.respond(matches)
        }
    }
}
