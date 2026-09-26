package com.skillx.core.extensions

/**
 * Collection utility extensions used across features.
 */

/**
 * Find the first matching element between two lists using case-insensitive comparison.
 * Extracted from findMatchingSkill() in MainActivity.kt.
 * Returns the element from [other] if a match is found.
 */
fun List<String>.findFirstCaseInsensitiveMatch(other: List<String>): String? {
    for (mine in this) {
        for (theirs in other) {
            if (mine.trim().equals(theirs.trim(), ignoreCase = true)) {
                return theirs
            }
        }
    }
    return null
}
