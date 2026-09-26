package com.skillx.features.matching.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.matching.domain.model.SkillMatch
import kotlinx.coroutines.flow.Flow

/**
 * Match repository interface.
 * Client calls server API — matching computation is server-side only.
 */
interface MatchRepository {
    suspend fun findMatches(): AppResult<List<SkillMatch>, AppError>
    fun observeLiveMatches(): Flow<List<SkillMatch>>
}
