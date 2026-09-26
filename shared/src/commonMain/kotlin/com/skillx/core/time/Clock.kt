package com.skillx.core.time

/**
 * Multiplatform clock abstraction.
 * Provides current time without platform-specific imports in domain code.
 */
expect object Clock {
    fun currentTimeMillis(): Long
}
