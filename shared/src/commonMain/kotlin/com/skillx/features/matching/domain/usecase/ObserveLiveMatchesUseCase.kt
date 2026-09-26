package com.skillx.features.matching.domain.usecase

import com.skillx.features.matching.domain.model.SkillMatch
import com.skillx.features.matching.domain.repository.MatchRepository
import kotlinx.coroutines.flow.Flow

/**
 * Observes live skill matches via WebSocket stream.
 */
class ObserveLiveMatchesUseCase(
    private val matchRepository: MatchRepository
) {
    operator fun invoke(): Flow<List<SkillMatch>> {
        return matchRepository.observeLiveMatches()
    }
}
