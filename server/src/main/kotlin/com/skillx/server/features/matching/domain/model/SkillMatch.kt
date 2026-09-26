package com.skillx.server.features.matching.domain.model
data class SkillMatch(val uid: String, val name: String, val email: String, val matchedSkill: String, val teachSkills: List<String>, val learnSkills: List<String>)
