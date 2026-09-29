package com.skillx.server.features.search.domain.model

/**
 * Public-safe view of a user for discovery — deliberately excludes email and any other
 * private profile data.
 */
data class PersonSummary(
    val uid: String,
    val name: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>
)
