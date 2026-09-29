package com.skillx.features.onboarding.data.mapper

import com.skillx.features.onboarding.data.dto.LanguageProficiencyEntryDto
import com.skillx.features.onboarding.data.dto.OnboardingProgressDto
import com.skillx.features.onboarding.data.dto.UserSkillEntryDto
import com.skillx.features.onboarding.domain.model.AvailabilityDay
import com.skillx.features.onboarding.domain.model.CefrLevel
import com.skillx.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.features.onboarding.domain.model.LearningGoal
import com.skillx.features.onboarding.domain.model.LessonFormat
import com.skillx.features.onboarding.domain.model.OnboardingProgress
import com.skillx.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.features.onboarding.domain.model.SkillRelation
import com.skillx.features.onboarding.domain.model.TeachingGoal
import com.skillx.features.onboarding.domain.model.TimeOfDay
import com.skillx.features.onboarding.domain.model.UserSkillEntry

object OnboardingMapper {

    fun toDomain(dto: OnboardingProgressDto): OnboardingProgress {
        return OnboardingProgress(
            userId = com.skillx.core.identifiers.UserId(dto.userId),
            teachSkills = dto.teachSkills.map { toDomain(it) },
            learnSkills = dto.learnSkills.map { toDomain(it) },
            learningGoals = dto.learningGoals.map { LearningGoal.valueOf(it) }.toSet(),
            teachingGoals = dto.teachingGoals.map { TeachingGoal.valueOf(it) }.toSet(),
            availableDays = dto.availableDays.map { AvailabilityDay.valueOf(it) }.toSet(),
            availableTimesOfDay = dto.availableTimesOfDay.map { TimeOfDay.valueOf(it) }.toSet(),
            lessonFormats = dto.lessonFormats.map { LessonFormat.valueOf(it) }.toSet(),
            preferredDurationMinutes = dto.preferredDurationMinutes,
            languages = dto.languages.map { toDomain(it) },
            completedAtEpochMillis = dto.completedAtEpochMillis
        )
    }

    fun toDto(entry: com.skillx.features.onboarding.domain.model.UserSkillEntry): UserSkillEntryDto {
        return UserSkillEntryDto(
            skillId = entry.skillId.value,
            relation = entry.relation.name,
            proficiency = entry.proficiency?.name,
            targetProficiency = entry.targetProficiency?.name
        )
    }

    fun toDomain(dto: UserSkillEntryDto): com.skillx.features.onboarding.domain.model.UserSkillEntry {
        return com.skillx.features.onboarding.domain.model.UserSkillEntry(
            skillId = com.skillx.core.identifiers.SkillId(dto.skillId),
            relation = SkillRelation.valueOf(dto.relation),
            proficiency = dto.proficiency?.let { ProficiencyLevel.valueOf(it) },
            targetProficiency = dto.targetProficiency?.let { ProficiencyLevel.valueOf(it) }
        )
    }

    fun toDomain(dto: LanguageProficiencyEntryDto): LanguageProficiencyEntry {
        return LanguageProficiencyEntry(
            languageCode = dto.languageCode,
            level = CefrLevel.valueOf(dto.level)
        )
    }
}