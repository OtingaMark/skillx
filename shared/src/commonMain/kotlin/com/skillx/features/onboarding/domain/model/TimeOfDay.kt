package com.skillx.features.onboarding.domain.model

/**
 * Part of the day a user is available for lessons.
 * [pluralLabel] is used in summaries ("Mon, Wed • Evenings • 30 min").
 */
enum class TimeOfDay(val displayName: String, val pluralLabel: String) {
    MORNING("Morning", "Mornings"),
    AFTERNOON("Afternoon", "Afternoons"),
    EVENING("Evening", "Evenings"),
    NIGHT("Night", "Nights")
}
