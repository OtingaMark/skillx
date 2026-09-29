package com.skillx.features.onboarding.data.dto

import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.CefrLevel
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.features.onboarding.domain.model.SkillRelation
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.model.TimeOfDay
import com.skillx.features.onboarding.domain.model.UserSkillEntry
import kotlinx.serialization.Serializable

@Serializable data class OnboardingProgressDto(
    val userId: String,
    val teachSkills: List<UserSkillEntryDto> = emptyList(),
    val learnSkills: List<UserSkillEntryDto> = emptyList(),
    val learningGoals: List<String> = emptyList(),
    val teachingGoals: List<String> = emptyList(),
    val availableDays: List<String> = emptyList(),
    val availableTimesOfDay: List<String> = emptyList(),
    val lessonFormats: List<String> = emptyList(),
    val preferredDurationMinutes: Int? = null,
    val languages: List<LanguageProficiencyEntryDto> = emptyList(),
    val completedAtEpochMillis: Long? = null
)

@Serializable data class UserSkillEntryDto(
    val skillId: String,
    val relation: String,
    val proficiency: String?,
    val targetProficiency: String?
) {
    fun toDomain(): com.skillx.features.onboarding.domain.model.UserSkillEntry {
        return UserSkillEntry(
            skillId = com.skillx.core.identifiers.SkillId(skillId),
            relation = SkillRelation.valueOf(relation),
            proficiency = proficiency?.let { ProficiencyLevel.valueOf(it) },
            targetProficiency = targetProficiency?.let { ProficiencyLevel.valueOf(it) }
        )
    }
}

@Serializable data class LanguageProficiencyEntryDto(
    val languageCode: String,
    val level: String
) {
    fun toDomain(): com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry {
        return LanguageProficiencyEntry(
            languageCode = languageCode,
            level = CefrLevel.valueOf(level)
        )
    }
}

@Serializable data class SaveTeachSkillsRequestDto(
    val skills: List<UserSkillEntryDto>
) {
    fun toDomainEntries(): List<com.skillx.features.onboarding.domain.model.UserSkillEntry> {
        return skills.map { it.toDomain() }
    }
}

@Serializable data class SaveLearnSkillsRequestDto(
    val skills: List<UserSkillEntryDto>
) {
    fun toDomainEntries(): List<com.skillx.features.onboarding.domain.model.UserSkillEntry> {
        return skills.map { it.toDomain() }
    }
}

@Serializable data class SaveProficiencyRequestDto(
    val skills: List<UserSkillEntryDto>
) {
    fun toDomainEntries(): List<com.skillx.features.onboarding.domain.model.UserSkillEntry> {
        return skills.map { it.toDomain() }
    }
}

@Serializable data class SaveGoalsRequestDto(
    val learningGoals: List<String>,
    val teachingGoals: List<String>
) {
    fun learningGoalsSet(): Set<com.skillx.features.onboarding.domain.model.LearningGoal> {
        return learningGoals.map { LearningGoal.valueOf(it) }.toSet()
    }
    fun teachingGoalsSet(): Set<com.skillx.features.onboarding.domain.model.TeachingGoal> {
        return teachingGoals.map { TeachingGoal.valueOf(it) }.toSet()
    }
}

@Serializable data class SaveAvailabilityRequestDto(
    val days: List<String>,
    val timesOfDay: List<String>,
    val lessonFormats: List<String>,
    val preferredDurationMinutes: Int?,
    val languages: List<LanguageProficiencyEntryDto>
) {
    fun daysSet(): Set<com.skillx.features.onboarding.domain.model.AvailabilityDay> {
        return days.map { AvailabilityDay.valueOf(it) }.toSet()
    }
    fun timesOfDaySet(): Set<TimeOfDay> {
        return timesOfDay.map { TimeOfDay.valueOf(it) }.toSet()
    }
    fun lessonFormatsSet(): Set<com.skillx.features.onboarding.domain.model.LessonFormat> {
        return lessonFormats.map { LessonFormat.valueOf(it) }.toSet()
    }
    fun languagesList(): List<com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry> {
        return languages.map { it.toDomain() }
    }
}