package com.skillx.server.features.search.application.usecase

import com.skillx.server.features.search.domain.model.SkillStat
import com.skillx.server.features.search.domain.repository.SearchRepository
import com.skillx.server.features.search.domain.service.SkillCatalog

/** Skills whose name contains the query (case-insensitive), most popular first. Blank query → most popular overall. */
class SearchSkillsUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(query: String, limit: Int? = null): List<SkillStat> {
        val q = SearchQuery.normalizeQuery(query)
        return SkillCatalog.aggregate(repository.loadPeople())
            .filter { q.isEmpty() || it.id.contains(q) }
            .sortedWith(SkillCatalog.byPopularity)
            .take(SearchQuery.clampLimit(limit))
    }
}
