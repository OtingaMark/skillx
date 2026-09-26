package com.skillx.server.features.users.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class UserResponse(val id: String, val name: String, val email: String, val teachSkills: List<String>, val learnSkills: List<String>, val points: Int)
