package com.skillx.core.identifiers

import kotlin.jvm.JvmInline

/**
 * Type-safe wrapper for lesson request identifiers.
 */
@JvmInline
value class LessonId(val value: String) {
    init {
        require(value.isNotBlank()) { "LessonId cannot be blank" }
    }

    override fun toString(): String = value
}
