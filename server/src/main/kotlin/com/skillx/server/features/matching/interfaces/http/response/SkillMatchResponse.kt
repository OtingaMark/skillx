package com.skillx.server.features.matching.interfaces.http.response
import kotlinx.serialization.Serializable
@Serializable data class SkillMatchResponse(val uid: String, val name: String, val email: String, val matchedSkill: String, val teachSkills: List<String>, val learnSkills: List<String>)
