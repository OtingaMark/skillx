package com.skillx.server.features.search.domain.repository

import com.skillx.server.features.search.domain.model.PersonSummary

interface SearchRepository {
    /** Every user's public-safe summary — the source both skill and people search derive from. */
    suspend fun loadPeople(): List<PersonSummary>
}
