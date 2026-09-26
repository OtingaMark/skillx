package com.skillx.features.skills.domain.model

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class SkillTest {
    @Test fun matchesCaseInsensitive() = assertTrue(Skill("Kotlin").matches(Skill("kotlin")))
    @Test fun matchesTrimmed() = assertTrue(Skill(" Kotlin ").matches(Skill("kotlin")))
    @Test fun noMatch() = assertFalse(Skill("Kotlin").matches(Skill("Java")))
}
