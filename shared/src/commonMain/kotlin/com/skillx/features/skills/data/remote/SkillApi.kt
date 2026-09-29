package com.skillx.features.skills.data.remote

import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

@Serializable
data class SaveSkillsRequestDto(val teachSkills: List<String>, val learnSkills: List<String>)

@Serializable
data class SkillsResponseDto(val teachSkills: List<String>, val learnSkills: List<String>)

@Serializable
data class SkillDto(val id: String, val name: String)

class SkillApi(private val client: HttpClient) {
    suspend fun getSkills(): HttpResponse = client.get("/api/v1/skills")
    suspend fun saveSkills(request: SaveSkillsRequestDto): HttpResponse = client.put("/api/v1/skills") { setBody(request) }
    suspend fun searchSkills(query: String): HttpResponse = client.get("/api/v1/skills/search") { parameter("query", query) }
}
