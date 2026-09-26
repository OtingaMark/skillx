package com.skillx.features.matching.domain.model

/**
 * Domain model representing a skill match between two students.
 * Extracted from data class SkillMatch in MainActivity.kt (L2503-2510).
 * Pure Kotlin — no Firebase or Compose imports.
 */
data class SkillMatch(
    val uid: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val matchedSkill: String
)
