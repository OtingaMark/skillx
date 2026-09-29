package com.skillx.features.onboarding.domain.model

import com.skillx.core.identifiers.SkillId

/**
 * Represents a user's skill entry during onboarding.
 * For teach skills: proficiency is required.
 * For learn skills: targetProficiency is optional (desired level).
 */
data class UserSkillEntry(
    val skillId: SkillId,
    val relation: SkillRelation,
    val proficiency: ProficiencyLevel? = null,
    val targetProficiency: ProficiencyLevel? = null
)