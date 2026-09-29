package com.skillx.server.features.search.interfaces.http.response

import com.skillx.server.features.search.domain.model.PersonSummary
import kotlinx.serialization.Serializable

@Serializable
data class PersonSummaryResponse(
    val uid: String,
    val name: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>
)

fun PersonSummary.toResponse() = PersonSummaryResponse(uid, name, teachSkills, learnSkills)
