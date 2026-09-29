package com.skillx.features.onboarding.domain.model

/**
 * Enum representing CEFR language proficiency levels.
 * The UI always shows displayLabel, never the raw code.
 */
enum class CefrLevel(val displayLabel: String) {
    A1("Beginner"),
    A2("Elementary"),
    B1("Intermediate"),
    B2("Upper Intermediate"),
    C1("Advanced"),
    C2("Fluent")
}