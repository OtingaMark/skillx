package com.skillx.server.features.search.application.usecase

import com.skillx.server.features.search.domain.model.PersonSummary
import com.skillx.server.features.search.domain.repository.SearchRepository

/**
 * People whose name or teach/learn skills contain the query, excluding the requester.
 * People who can teach the query rank first, then skill-learners, then name matches.
 * A blank query returns nothing — discovery without a query is the matching feature's job.
 */
class SearchPeopleUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(requesterId: String, query: String, limit: Int? = null): List<PersonSummary> {
        val q = SearchQuery.normalizeQuery(query)
        if (q.isEmpty()) return emptyList()

        return repository.loadPeople()
            .asSequence()
            .filter { it.uid != requesterId }
            .mapNotNull { person -> rank(person, q)?.let { person to it } }
            .sortedWith(compareBy<Pair<PersonSummary, Int>> { it.second }.thenBy { it.first.name.lowercase() })
            .map { it.first }
            .take(SearchQuery.clampLimit(limit))
            .toList()
    }

    private fun rank(person: PersonSummary, q: String): Int? = when {
        person.teachSkills.any { it.lowercase().contains(q) } -> 0
        person.learnSkills.any { it.lowercase().contains(q) } -> 1
        person.name.lowercase().contains(q) -> 2
        else -> null
    }
}
