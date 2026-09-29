package com.skillx.server.features.search.application.usecase

import com.skillx.server.features.search.domain.model.PersonSummary
import com.skillx.server.features.search.domain.repository.SearchRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class LoadPopularSkillsUseCaseTest {

    @Test
    fun `only teachable skills, most taught first`() = runBlocking {
        val repository = mockk<SearchRepository>()
        coEvery { repository.loadPeople() } returns listOf(
            PersonSummary("a", "Ann", listOf("Guitar", "Python"), listOf("Chess")),
            PersonSummary("b", "Bob", listOf("Python"), listOf("Chess"))
        )

        val results = LoadPopularSkillsUseCase(repository)()

        assertEquals(listOf("Python", "Guitar"), results.map { it.name })
    }
}
