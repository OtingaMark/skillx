package com.skillx.features.matching.data.dto
import kotlinx.serialization.Serializable
@Serializable data class SkillMatchDto(
    val uid: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val matchedSkill: String
)
