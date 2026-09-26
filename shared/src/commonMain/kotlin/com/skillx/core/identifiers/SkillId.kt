package com.skillx.core.identifiers

import kotlin.jvm.JvmInline

/**
 * Type-safe wrapper for skill identifiers.
 */
@JvmInline
value class SkillId(val value: String) {
    init {
        require(value.isNotBlank()) { "SkillId cannot be blank" }
    }

    override fun toString(): String = value
}
