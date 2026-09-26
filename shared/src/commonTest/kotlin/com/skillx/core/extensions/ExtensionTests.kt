package com.skillx.core.extensions

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class StringExtensionsTest {
    @Test fun toSkillListBasic() = assertEquals(listOf("Kotlin", "Java"), "Kotlin, Java".toSkillList())
    @Test fun toSkillListWithDuplicates() = assertEquals(listOf("Kotlin"), "Kotlin, Kotlin".toSkillList())
    @Test fun toSkillListWithNewlines() = assertEquals(listOf("A", "B"), "A\nB".toSkillList())
    @Test fun toSkillListEmpty() = assertEquals(emptyList(), "".toSkillList())
}

class CollectionExtensionsTest {
    @Test fun findMatch() = assertEquals("Java", listOf("kotlin", "java").findFirstCaseInsensitiveMatch(listOf("Java", "Python")))
    @Test fun noMatch() = assertNull(listOf("kotlin").findFirstCaseInsensitiveMatch(listOf("Java")))
}
