package com.skillx.features.onboarding.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a user's language proficiency entry during onboarding.
 * Uses ISO 639-1 language codes and CEFR levels.
 */
@Serializable
data class LanguageProficiencyEntry(
    val languageCode: String,
    val level: CefrLevel
)