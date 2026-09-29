package com.skillx.server.features.search.domain.model

/**
 * A skill as it exists across the community: how many distinct users teach it and
 * how many want to learn it. Derived from users' teach/learn skill lists — there is no
 * separate skills collection, so this is the catalog.
 */
data class SkillStat(
    val id: String,
    val name: String,
    val teacherCount: Int,
    val learnerCount: Int
)
