package com.skillx.core.extensions

/**
 * String utility extensions used across the codebase.
 * Extracted from parseSkills() and getSkillsFromFirestore() in MainActivity.kt.
 */

/**
 * Parse a comma-or-newline-separated skill string into a clean, deduplicated list.
 * Preserves the exact behavior of the original parseSkills() function.
 */
fun String.toSkillList(): List<String> {
    return this
        .split(",", "\n")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .distinct()
}

/**
 * Normalize a skill name for case-insensitive comparison.
 */
fun String.normalizeSkillName(): String {
    return this.trim().lowercase()
}
