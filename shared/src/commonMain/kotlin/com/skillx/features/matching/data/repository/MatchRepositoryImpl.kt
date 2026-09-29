package com.skillx.features.matching.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.matching.data.remote.MatchApi
import com.skillx.features.matching.data.dto.SkillMatchDto
import com.skillx.features.matching.domain.model.SkillMatch
import com.skillx.features.matching.domain.repository.MatchRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MatchRepositoryImpl(
    private val matchApi: MatchApi
) : MatchRepository {

    override suspend fun findMatches(): AppResult<List<SkillMatch>, AppError> {
        return try {
            val response = matchApi.findMatches()
            if (response.status.isSuccess()) {
                val dtos = response.body<List<SkillMatchDto>>()
                AppResult.Success(dtos.map {
                    SkillMatch(uid = it.uid, name = it.name, email = it.email,
                        teachSkills = it.teachSkills, learnSkills = it.learnSkills,
                        matchedSkill = it.matchedSkill)
                })
            } else {
                val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }
                AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err))
            }
        } catch (e: Exception) {
            AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to find matches."))
        }
    }

    override fun observeLiveMatches(): Flow<List<SkillMatch>> {
        // WebSocket implementation will be added in a future iteration
        return flowOf(emptyList())
    }
}
