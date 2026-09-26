package com.skillx.features.matching.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult
import com.skillx.features.matching.domain.model.SkillMatch
import com.skillx.features.matching.domain.repository.MatchRepository

/**
 * Finds skill matches for the current user.
 * The matching computation happens server-side — the client just receives results.
 */
class FindSkillMatchesUseCase(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(): AppResult<List<SkillMatch>, AppError> {
        return matchRepository.findMatches()
    }
}
