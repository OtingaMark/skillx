package com.skillx.server.features.search.application.usecase

import com.skillx.server.core.exceptions.ValidationException
import com.skillx.server.features.search.domain.model.PersonSummary
import com.skillx.server.features.search.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SearchSkillsUseCaseTest {

    private val repository = mockk<SearchRepository>().also {
        coEvery { it.loadPeople() } returns listOf(
            PersonSummary("a", "Ann", listOf("Python", "Guitar"), emptyList()),
            PersonSummary("b", "Bob", listOf("Python"), listOf("Spanish")),
            PersonSummary("c", "Cat", listOf("Photography"), listOf("Python"))
        )
    }

    @Test
    fun `matches substrings case-insensitively, most taught first`() = runBlocking {
        val results = SearchSkillsUseCase(repository)("PYT")
        assertEquals(listOf("Python"), results.map { it.name })
        assertEquals(2, results.single().teacherCount)
        assertEquals(1, results.single().learnerCount)
    }

    @Test
    fun `blank query returns everything ordered by popularity`() = runBlocking {
        val results = SearchSkillsUseCase(repository)("  ")
        assertEquals("Python", results.first().name)
        assertEquals(4, results.size)
    }

    @Test
    fun `limit is clamped`() = runBlocking {
        assertEquals(1, SearchSkillsUseCase(repository)("", limit = 0).size)
    }

    @Test
    fun `overlong query is rejected`() {
        assertFailsWith<ValidationException> {
            runBlocking { SearchSkillsUseCase(repository)("x".repeat(101)) }
        }
        Unit
    }
}
