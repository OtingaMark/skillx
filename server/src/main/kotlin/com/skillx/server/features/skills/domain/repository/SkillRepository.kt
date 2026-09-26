package com.skillx.server.features.skills.domain.repository
interface SkillRepository { suspend fun getSkills(userId: String): Pair<List<String>, List<String>>; suspend fun updateSkills(userId: String, teach: List<String>, learn: List<String>) }
