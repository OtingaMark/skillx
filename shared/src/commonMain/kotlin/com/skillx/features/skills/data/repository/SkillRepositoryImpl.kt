package com.skillx.features.skills.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.skills.data.remote.SaveSkillsRequestDto
import com.skillx.features.skills.data.remote.SkillApi
import com.skillx.features.skills.data.remote.SkillDto
import com.skillx.features.skills.data.remote.SkillsResponseDto
import com.skillx.features.skills.domain.model.Skill
import com.skillx.features.skills.domain.repository.SkillRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*

class SkillRepositoryImpl(private val api: SkillApi) : SkillRepository {
    private var cachedTeach: MutableList<String> = mutableListOf()
    private var cachedLearn: MutableList<String> = mutableListOf()

    override suspend fun addTeachingSkill(skill: String): AppResult<List<String>, AppError> {
        if (!cachedTeach.any { it.equals(skill, ignoreCase = true) }) cachedTeach.add(skill)
        return saveSkills(cachedTeach, cachedLearn).map { cachedTeach.toList() }
    }
    override suspend fun removeTeachingSkill(skill: String): AppResult<List<String>, AppError> {
        cachedTeach.removeAll { it == skill }
        return saveSkills(cachedTeach, cachedLearn).map { cachedTeach.toList() }
    }
    override suspend fun addLearningSkill(skill: String): AppResult<List<String>, AppError> {
        if (!cachedLearn.any { it.equals(skill, ignoreCase = true) }) cachedLearn.add(skill)
        return saveSkills(cachedTeach, cachedLearn).map { cachedLearn.toList() }
    }
    override suspend fun removeLearningSkill(skill: String): AppResult<List<String>, AppError> {
        cachedLearn.removeAll { it == skill }
        return saveSkills(cachedTeach, cachedLearn).map { cachedLearn.toList() }
    }
    override suspend fun getTeachingSkills(): AppResult<List<String>, AppError> = loadSkills().map { it.first }
    override suspend fun getLearningSkills(): AppResult<List<String>, AppError> = loadSkills().map { it.second }

    override suspend fun saveSkills(teachSkills: List<String>, learnSkills: List<String>): AppResult<Unit, AppError> {
        return try {
            val response = api.saveSkills(SaveSkillsRequestDto(teachSkills, learnSkills))
            if (response.status.isSuccess()) { cachedTeach = teachSkills.toMutableList(); cachedLearn = learnSkills.toMutableList(); AppResult.Success(Unit) }
            else { val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }; AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err)) }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to save skills.")) }
    }

    override suspend fun search(query: String): List<Skill> {
        return try {
            val response = api.searchSkills(query)
            if (response.status.isSuccess()) {
                response.body<List<SkillDto>>().map { Skill(name = it.name, id = com.skillx.core.identifiers.SkillId(it.id)) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun loadSkills(): AppResult<Pair<List<String>, List<String>>, AppError> {
        return try {
            val response = api.getSkills()
            if (response.status.isSuccess()) {
                val dto = response.body<SkillsResponseDto>()
                cachedTeach = dto.teachSkills.toMutableList(); cachedLearn = dto.learnSkills.toMutableList()
                AppResult.Success(dto.teachSkills to dto.learnSkills)
            } else { val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }; AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err)) }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load skills.")) }
    }
}
