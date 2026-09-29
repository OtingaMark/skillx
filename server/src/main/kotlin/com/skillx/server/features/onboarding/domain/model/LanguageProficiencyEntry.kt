package com.skillx.server.features.onboarding.domain.model

data class LanguageProficiencyEntry(
    val languageCode: String,
    val level: CefrLevel
)