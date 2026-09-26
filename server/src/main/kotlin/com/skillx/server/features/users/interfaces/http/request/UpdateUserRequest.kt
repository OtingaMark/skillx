package com.skillx.server.features.users.interfaces.http.request
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRequest(
    val name: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>
)
