package com.skillx.server.features.search.application.usecase

import com.skillx.server.features.search.domain.model.SkillStat
import com.skillx.server.features.search.domain.repository.SearchRepository
import com.skillx.server.features.search.domain.service.SkillCatalog

/** The most-taught skills in the community — only skills at least one person can actually teach. */
class LoadPopularSkillsUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(limit: Int? = null): List<SkillStat> =
        SkillCatalog.aggregate(repository.loadPeople())
            .filter { it.teacherCount > 0 }
            .sortedWith(SkillCatalog.byPopularity)
            .take(SearchQuery.clampLimit(limit))
}
