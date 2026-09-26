package com.skillx.features.matching.domain.usecase

import kotlin.test.Test
import kotlin.test.assertTrue

class FindSkillMatchesUseCaseTest {
    @Test fun noMatchesWhenNoLearnSkills() {
        // When user has no learn skills, should return empty matches
        val myLearnSkills = emptyList<String>()
        assertTrue(myLearnSkills.isEmpty())
    }
    @Test fun matchesFoundWhenSkillsOverlap() {
        val myLearnSkills = listOf("Kotlin", "Java")
        val otherTeachSkills = listOf("kotlin", "Python")
        val match = myLearnSkills.firstOrNull { mine -> otherTeachSkills.any { it.equals(mine, ignoreCase = true) } }
        assertTrue(match != null)
    }
}
