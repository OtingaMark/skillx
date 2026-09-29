package com.skillx.server.features.onboarding.interfaces.http.response

import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.server.features.onboarding.domain.model.SkillRelation
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.UserSkillEntry
import kotlinx.serialization.Serializable

@Serializable data class OnboardingProgressResponse(
    val userId: String,
    val teachSkills: List<UserSkillEntryResponse> = emptyList(),
    val learnSkills: List<UserSkillEntryResponse> = emptyList(),
    val learningGoals: List<String> = emptyList(),
    val teachingGoals: List<String> = emptyList(),
    val availableDays: List<String> = emptyList(),
    val availableTimesOfDay: List<String> = emptyList(),
    val lessonFormats: List<String> = emptyList(),
    val preferredDurationMinutes: Int? = null,
    val languages: List<LanguageProficiencyEntryResponse> = emptyList(),
    val completedAtEpochMillis: Long? = null
) {
    companion object {
        fun fromDomain(progress: com.skillx.server.features.onboarding.domain.model.OnboardingProgress): OnboardingProgressResponse {
            return OnboardingProgressResponse(
                userId = progress.userId,
                teachSkills = progress.teachSkills.map { UserSkillEntryResponse.fromDomain(it) },
                learnSkills = progress.learnSkills.map { UserSkillEntryResponse.fromDomain(it) },
                learningGoals = progress.learningGoals.map { it.name },
                teachingGoals = progress.teachingGoals.map { it.name },
                availableDays = progress.availableDays.map { it.name },
                availableTimesOfDay = progress.availableTimesOfDay.map { it.name },
                lessonFormats = progress.lessonFormats.map { it.name },
                preferredDurationMinutes = progress.preferredDurationMinutes,
                languages = progress.languages.map { LanguageProficiencyEntryResponse.fromDomain(it) },
                completedAtEpochMillis = progress.completedAtEpochMillis
            )
        }
    }
}

@Serializable data class UserSkillEntryResponse(
    val skillId: String,
    val relation: String,
    val proficiency: String?,
    val targetProficiency: String?
) {
    companion object {
        fun fromDomain(entry: com.skillx.server.features.onboarding.domain.model.UserSkillEntry): UserSkillEntryResponse {
            return UserSkillEntryResponse(
                skillId = entry.skillId,
                relation = entry.relation.name,
                proficiency = entry.proficiency?.name,
                targetProficiency = entry.targetProficiency?.name
            )
        }
    }
}

@Serializable data class LanguageProficiencyEntryResponse(
    val languageCode: String,
    val level: String
) {
    companion object {
        fun fromDomain(entry: com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry): LanguageProficiencyEntryResponse {
            return LanguageProficiencyEntryResponse(
                languageCode = entry.languageCode,
                level = entry.level.name
            )
        }
    }
}