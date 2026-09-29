package com.skillx.server.features.search.domain.service

import com.skillx.server.features.search.domain.model.PersonSummary
import kotlin.test.Test
import kotlin.test.assertEquals

class SkillCatalogTest {

    @Test
    fun `counts distinct teachers and learners case-insensitively`() {
        val people = listOf(
            PersonSummary("a", "Ann", teachSkills = listOf("Python", "python "), learnSkills = listOf("Guitar")),
            PersonSummary("b", "Bob", teachSkills = listOf("PYTHON"), learnSkills = listOf("python")),
            PersonSummary("c", "Cat", teachSkills = emptyList(), learnSkills = listOf("guitar"))
        )

        val stats = SkillCatalog.aggregate(people).associateBy { it.id }

        assertEquals(2, stats.getValue("python").teacherCount)
        assertEquals(1, stats.getValue("python").learnerCount)
        assertEquals("Python", stats.getValue("python").name)
        assertEquals(0, stats.getValue("guitar").teacherCount)
        assertEquals(2, stats.getValue("guitar").learnerCount)
    }

    @Test
    fun `ignores blank skill names`() {
        val stats = SkillCatalog.aggregate(listOf(PersonSummary("a", "Ann", listOf("  ", ""), listOf(" "))))
        assertEquals(emptyList(), stats)
    }
}
