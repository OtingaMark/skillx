package com.skillx.features.skills.data.dto
import kotlinx.serialization.Serializable
@Serializable data class SkillListRequestDto(val teachSkills: List<String>, val learnSkills: List<String>)
