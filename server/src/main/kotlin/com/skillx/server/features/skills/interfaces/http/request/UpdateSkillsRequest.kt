package com.skillx.server.features.skills.interfaces.http.request
import kotlinx.serialization.Serializable
@Serializable data class UpdateSkillsRequest(val teachSkills: List<String>, val learnSkills: List<String>)
