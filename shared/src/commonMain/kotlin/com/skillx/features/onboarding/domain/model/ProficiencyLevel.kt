package com.skillx.features.onboarding.domain.model

/**
 * Enum representing the proficiency level for a skill.
 * Used for both teach skills (current level) and learn skills (target level).
 */
enum class ProficiencyLevel(
    val displayName: String,
    val shortDescription: String
) {
    BEGINNER("Beginner", "I know the basics"),
    INTERMEDIATE("Intermediate", "I can do it independently"),
    ADVANCED("Advanced", "I can solve complex problems"),
    PROFESSIONAL("Professional", "I use it in real-world situations"),
    MASTER("Master", "I have exceptional expertise")
}