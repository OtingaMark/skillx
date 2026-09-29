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

/**
 * Formats a Double to one decimal place (e.g. "4.5") without java.util.Formatter,
 * which isn't available on Kotlin/Native (iOS).
 */
fun Double.toOneDecimalString(): String {
    val rounded = kotlin.math.round(this * 10)
    val whole = (rounded / 10).toInt()
    val decimal = kotlin.math.abs((rounded % 10).toInt())
    return "$whole.$decimal"
}
