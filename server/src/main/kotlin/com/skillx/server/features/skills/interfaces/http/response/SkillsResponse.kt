package com.skillx.server.features.skills.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class SkillsResponse(val teachSkills: List<String>, val learnSkills: List<String>)
