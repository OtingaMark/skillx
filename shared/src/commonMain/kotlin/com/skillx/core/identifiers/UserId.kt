package com.skillx.core.identifiers

import kotlin.jvm.JvmInline

/**
 * Type-safe wrapper for user identifiers.
 * Prevents accidentally passing a lesson ID where a user ID is expected.
 */
@JvmInline
value class UserId(val value: String) {
    init {
        require(value.isNotBlank()) { "UserId cannot be blank" }
    }

    override fun toString(): String = value
}
