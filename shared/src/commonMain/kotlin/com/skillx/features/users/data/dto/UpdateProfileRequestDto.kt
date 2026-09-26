package com.skillx.features.users.data.dto
import kotlinx.serialization.Serializable
@Serializable data class UpdateProfileRequestDto(val name: String, val teachSkills: List<String>, val learnSkills: List<String>)
