package com.skillx.server.features.onboarding.interfaces.http.request

import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.server.features.onboarding.domain.model.SkillRelation
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.onboarding.domain.model.UserSkillEntry
import kotlinx.serialization.Serializable

@Serializable data class SaveTeachSkillsRequest(
    val skills: List<UserSkillEntryRequest>
) {
    fun toEntries(): List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry> {
        return skills.map { it.toEntry() }
    }
}

@Serializable data class UserSkillEntryRequest(
    val skillId: String,
    val relation: String,
    val proficiency: String?,
    val targetProficiency: String?
) {
    fun toEntry(): UserSkillEntry {
        return UserSkillEntry(
            skillId = skillId,
            relation = SkillRelation.valueOf(relation),
            proficiency = proficiency?.let { ProficiencyLevel.valueOf(it) },
            targetProficiency = targetProficiency?.let { ProficiencyLevel.valueOf(it) }
        )
    }
}

@Serializable data class SaveLearnSkillsRequest(
    val skills: List<UserSkillEntryRequest>
) {
    fun toEntries(): List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry> {
        return skills.map { it.toEntry() }
    }
}

@Serializable data class SaveProficiencyRequest(
    val skills: List<UserSkillEntryRequest>
) {
    fun toEntries(): List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry> {
        return skills.map { it.toEntry() }
    }
}

@Serializable data class SaveGoalsRequest(
    val learningGoals: List<String>,
    val teachingGoals: List<String>
) {
    fun learningGoalsSet(): Set<com.skillx.server.features.onboarding.domain.model.LearningGoal> {
        return learningGoals.map { com.skillx.server.features.onboarding.domain.model.LearningGoal.valueOf(it) }.toSet()
    }
    fun teachingGoalsSet(): Set<com.skillx.server.features.onboarding.domain.model.TeachingGoal> {
        return teachingGoals.map { com.skillx.server.features.onboarding.domain.model.TeachingGoal.valueOf(it) }.toSet()
    }
}

@Serializable data class SaveAvailabilityRequest(
    val days: List<String>,
    // Defaulted so clients built before time-of-day existed still deserialize.
    val timesOfDay: List<String> = emptyList(),
    val lessonFormats: List<String>,
    val preferredDurationMinutes: Int?,
    val languages: List<LanguageProficiencyEntryRequest>
) {
    fun daysSet(): Set<com.skillx.server.features.onboarding.domain.model.AvailabilityDay> {
        return days.map { com.skillx.server.features.onboarding.domain.model.AvailabilityDay.valueOf(it) }.toSet()
    }
    fun timesOfDaySet(): Set<TimeOfDay> {
        return timesOfDay.map { TimeOfDay.valueOf(it) }.toSet()
    }
    fun lessonFormatsSet(): Set<com.skillx.server.features.onboarding.domain.model.LessonFormat> {
        return lessonFormats.map { com.skillx.server.features.onboarding.domain.model.LessonFormat.valueOf(it) }.toSet()
    }
    fun languagesList(): List<com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry> {
        return languages.map { it.toEntry() }
    }
}

@Serializable data class LanguageProficiencyEntryRequest(
    val languageCode: String,
    val level: String
) {
    fun toEntry(): com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry {
        return com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry(
            languageCode = languageCode,
            level = CefrLevel.valueOf(level)
        )
    }
}