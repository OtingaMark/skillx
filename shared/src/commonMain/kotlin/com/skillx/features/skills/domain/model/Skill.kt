package com.skillx.features.skills.domain.model

/**
 * Domain model representing a skill that can be taught or learned.
 */
data class Skill(
    val name: String
) {
    /**
     * Normalize the skill name for case-insensitive matching.
     */
    fun normalized(): String = name.trim().lowercase()

    /**
     * Check if this skill matches another by name (case-insensitive).
     */
    fun matches(other: Skill): Boolean {
        return this.normalized() == other.normalized()
    }
}
