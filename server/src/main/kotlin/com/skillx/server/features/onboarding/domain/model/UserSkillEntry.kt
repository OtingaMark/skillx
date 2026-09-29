package com.skillx.server.features.onboarding.domain.model

data class UserSkillEntry(
    val skillId: String,
    val relation: SkillRelation,
    val proficiency: ProficiencyLevel? = null,
    val targetProficiency: ProficiencyLevel? = null
)