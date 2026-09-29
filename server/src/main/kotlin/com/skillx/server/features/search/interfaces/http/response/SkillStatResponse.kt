package com.skillx.server.features.search.interfaces.http.response

import com.skillx.server.features.search.domain.model.SkillStat
import kotlinx.serialization.Serializable

@Serializable
data class SkillStatResponse(
    val id: String,
    val name: String,
    val teacherCount: Int,
    val learnerCount: Int
)

fun SkillStat.toResponse() = SkillStatResponse(id, name, teacherCount, learnerCount)
