package com.skillx.server.features.search.application.usecase

import com.skillx.server.features.search.domain.model.PersonSummary
import com.skillx.server.features.search.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchPeopleUseCaseTest {

    private val repository = mockk<SearchRepository>().also {
        coEvery { it.loadPeople() } returns listOf(
            PersonSummary("me", "Guitar Fan", listOf("Guitar"), emptyList()),
            PersonSummary("n", "Nora", emptyList(), listOf("Guitar")),
            PersonSummary("t", "Tom", listOf("Guitar"), emptyList()),
            PersonSummary("g", "Guitar Gabe", emptyList(), emptyList()),
            PersonSummary("x", "Xavier", listOf("Chess"), emptyList())
        )
    }

    @Test
    fun `excludes the requester and ranks teachers, then learners, then name matches`() = runBlocking {
        val results = SearchPeopleUseCase(repository)("me", "guitar")
        assertEquals(listOf("t", "n", "g"), results.map { it.uid })
    }

    @Test
    fun `blank query returns no one`() = runBlocking {
        assertEquals(emptyList(), SearchPeopleUseCase(repository)("me", "   "))
    }
}
