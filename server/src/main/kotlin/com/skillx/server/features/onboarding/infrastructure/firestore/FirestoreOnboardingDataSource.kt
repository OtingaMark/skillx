package com.skillx.server.features.onboarding.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.Transaction
import com.skillx.server.infrastructure.firestore.await
import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.OnboardingProgress
import com.skillx.server.features.onboarding.domain.model.ProficiencyLevel
import com.skillx.server.features.onboarding.domain.model.SkillRelation
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.onboarding.domain.model.UserSkillEntry
import com.skillx.server.features.onboarding.domain.repository.OnboardingRepository
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider

/**
 * Firestore implementation of OnboardingRepository.
 * Stores onboarding progress as a document in users/{uid}/onboarding/progress.
 */
class FirestoreOnboardingDataSource(
    private val provider: FirestoreClientProvider
) : OnboardingRepository {

    private val db = provider.getFirestore()

    private fun progressRef(userId: String) = db.collection("users").document(userId).collection("onboarding").document("progress")

    override suspend fun load(userId: String): OnboardingProgress {
        val doc = progressRef(userId).get().await()
        return if (doc.exists()) fromFirestore(doc) else OnboardingProgress(userId = userId)
    }

    override suspend fun saveTeachSkills(userId: String, entries: List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry>): OnboardingProgress {
        return updateProgress(userId) { current ->
            current.copy(teachSkills = entries)
        }
    }

    override suspend fun saveLearnSkills(userId: String, entries: List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry>): OnboardingProgress {
        return updateProgress(userId) { current ->
            current.copy(learnSkills = entries)
        }
    }

    override suspend fun saveProficiency(userId: String, entries: List<com.skillx.server.features.onboarding.domain.model.UserSkillEntry>): OnboardingProgress {
        // Merge proficiency entries into existing teach/learn skills
        return updateProgress(userId) { current ->
            val teachMap = current.teachSkills.associateBy { it.skillId }
            val learnMap = current.learnSkills.associateBy { it.skillId }
            val updatedTeach = current.teachSkills.map { skill ->
                entries.find { it.skillId == skill.skillId && it.relation == SkillRelation.TEACH }
                    ?.let { it.copy(proficiency = it.proficiency) }
                    ?: skill
            }
            val updatedLearn = current.learnSkills.map { skill ->
                entries.find { it.skillId == skill.skillId && it.relation == SkillRelation.LEARN }
                    ?.let { it.copy(targetProficiency = it.targetProficiency) }
                    ?: skill
            }
            current.copy(teachSkills = updatedTeach, learnSkills = updatedLearn)
        }
    }

    override suspend fun saveGoals(
        userId: String,
        learning: Set<com.skillx.server.features.onboarding.domain.model.LearningGoal>,
        teaching: Set<com.skillx.server.features.onboarding.domain.model.TeachingGoal>
    ): OnboardingProgress {
        return updateProgress(userId) { current ->
            current.copy(learningGoals = learning, teachingGoals = teaching)
        }
    }

    override suspend fun saveAvailability(
        userId: String,
        days: Set<com.skillx.server.features.onboarding.domain.model.AvailabilityDay>,
        timesOfDay: Set<TimeOfDay>,
        formats: Set<com.skillx.server.features.onboarding.domain.model.LessonFormat>,
        durationMinutes: Int?,
        languages: List<com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry>
    ): OnboardingProgress {
        return updateProgress(userId) { current ->
            current.copy(
                availableDays = days,
                availableTimesOfDay = timesOfDay,
                lessonFormats = formats,
                preferredDurationMinutes = durationMinutes,
                languages = languages
            )
        }
    }

    override suspend fun markCompleted(userId: String): OnboardingProgress {
        return updateProgress(userId) { current ->
            current.copy(completedAtEpochMillis = System.currentTimeMillis())
        }
    }

    private suspend fun updateProgress(userId: String, transform: (OnboardingProgress) -> OnboardingProgress): OnboardingProgress {
        val ref = progressRef(userId)
        return db.runTransaction { tx ->
            val doc = tx.get(ref).get()
            val current = if (doc.exists()) fromFirestore(doc) else OnboardingProgress(userId = userId)
            val updated = transform(current)
            val data = toFirestore(updated)
            tx.set(ref, data)
            updated
        }.await()
    }

    private fun fromFirestore(doc: DocumentSnapshot): OnboardingProgress {
        val data = doc.data ?: return OnboardingProgress(userId = doc.id)
        return OnboardingProgress(
            userId = doc.id,
            teachSkills = (data["teachSkills"] as? List<Map<String, Any>>)?.map { parseSkillEntry(it) } ?: emptyList(),
            learnSkills = (data["learnSkills"] as? List<Map<String, Any>>)?.map { parseSkillEntry(it) } ?: emptyList(),
            learningGoals = (data["learningGoals"] as? List<String>)?.map { LearningGoal.valueOf(it) }?.toSet() ?: emptySet(),
            teachingGoals = (data["teachingGoals"] as? List<String>)?.map { TeachingGoal.valueOf(it) }?.toSet() ?: emptySet(),
            availableDays = (data["availableDays"] as? List<String>)?.map { AvailabilityDay.valueOf(it) }?.toSet() ?: emptySet(),
            availableTimesOfDay = (data["availableTimesOfDay"] as? List<String>)?.map { TimeOfDay.valueOf(it) }?.toSet() ?: emptySet(),
            lessonFormats = (data["lessonFormats"] as? List<String>)?.map { LessonFormat.valueOf(it) }?.toSet() ?: emptySet(),
            preferredDurationMinutes = (data["preferredDurationMinutes"] as? Number)?.toInt(),
            languages = (data["languages"] as? List<Map<String, Any>>)?.map { parseLanguageEntry(it) } ?: emptyList(),
            completedAtEpochMillis = (data["completedAtEpochMillis"] as? Number)?.toLong()
        )
    }

    private fun toFirestore(progress: OnboardingProgress): Map<String, Any?> = mapOf(
        "teachSkills" to progress.teachSkills.map { toMap(it) },
        "learnSkills" to progress.learnSkills.map { toMap(it) },
        "learningGoals" to progress.learningGoals.map { it.name },
        "teachingGoals" to progress.teachingGoals.map { it.name },
        "availableDays" to progress.availableDays.map { it.name },
        "availableTimesOfDay" to progress.availableTimesOfDay.map { it.name },
        "lessonFormats" to progress.lessonFormats.map { it.name },
        "preferredDurationMinutes" to progress.preferredDurationMinutes,
        "languages" to progress.languages.map { toMap(it) },
        "completedAtEpochMillis" to progress.completedAtEpochMillis
    )

    private fun parseSkillEntry(map: Map<String, Any>): com.skillx.server.features.onboarding.domain.model.UserSkillEntry {
        return com.skillx.server.features.onboarding.domain.model.UserSkillEntry(
            skillId = map["skillId"] as String,
            relation = SkillRelation.valueOf(map["relation"] as String),
            proficiency = (map["proficiency"] as? String)?.let { ProficiencyLevel.valueOf(it) },
            targetProficiency = (map["targetProficiency"] as? String)?.let { ProficiencyLevel.valueOf(it) }
        )
    }

    private fun parseLanguageEntry(map: Map<String, Any>): LanguageProficiencyEntry {
        return LanguageProficiencyEntry(
            languageCode = map["languageCode"] as String,
            level = CefrLevel.valueOf(map["level"] as String)
        )
    }

    private fun toMap(entry: com.skillx.server.features.onboarding.domain.model.UserSkillEntry): Map<String, Any?> = mapOf(
        "skillId" to entry.skillId,
        "relation" to entry.relation.name,
        "proficiency" to entry.proficiency?.name,
        "targetProficiency" to entry.targetProficiency?.name
    )

    private fun toMap(entry: LanguageProficiencyEntry): Map<String, Any> = mapOf(
        "languageCode" to entry.languageCode,
        "level" to entry.level.name
    )
}