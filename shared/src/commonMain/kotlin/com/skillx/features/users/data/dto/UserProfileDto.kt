package com.skillx.features.users.data.dto
import kotlinx.serialization.Serializable
@Serializable data class UserProfileDto(val id: String, val name: String, val email: String, val teachSkills: List<String>, val learnSkills: List<String>, val points: Int)
