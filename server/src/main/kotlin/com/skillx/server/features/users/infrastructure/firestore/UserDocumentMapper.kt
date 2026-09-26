package com.skillx.server.features.users.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.skillx.server.features.users.domain.model.User

object UserDocumentMapper {
    @Suppress("UNCHECKED_CAST")
    fun fromDocument(id: String, data: Map<String, Any?>): User {
        return User(
            id = id,
            name = (data["name"] as? String) ?: "",
            email = (data["email"] as? String) ?: "",
            teachSkills = (data["teachSkills"] as? List<String>) ?: emptyList(),
            learnSkills = (data["learnSkills"] as? List<String>) ?: emptyList(),
            points = ((data["points"] as? Number)?.toInt()) ?: 0
        )
    }
}
