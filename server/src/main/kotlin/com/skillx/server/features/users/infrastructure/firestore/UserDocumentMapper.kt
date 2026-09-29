package com.skillx.server.features.users.infrastructure.firestore

import com.google.cloud.firestore.DocumentSnapshot
import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.CefrLevel
import com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.users.domain.model.User

/**
 * Maps between User domain model and Firestore document format.
 * Keeps Firestore document structure isolated from domain logic.
 */
object UserDocumentMapper {

    /**
     * Converts a Firestore DocumentSnapshot to a User domain model.
     */
    fun fromFirestore(doc: DocumentSnapshot): User {
        return User(
            id = doc.id,
            name = doc.getString("name") ?: "",
            email = doc.getString("email") ?: "",
            teachSkills = (doc.get("teachSkills") as? List<String>) ?: emptyList(),
            learnSkills = (doc.get("learnSkills") as? List<String>) ?: emptyList(),
            points = (doc.getLong("points") ?: 0L).toInt(),
            passwordHash = doc.getString("passwordHash") ?: "",
            onboardingCompleted = doc.getBoolean("onboardingCompleted") ?: false,
            learningGoals = (doc.get("learningGoals") as? List<String>)?.map { LearningGoal.valueOf(it) }?.toSet() ?: emptySet(),
            teachingGoals = (doc.get("teachingGoals") as? List<String>)?.map { TeachingGoal.valueOf(it) }?.toSet() ?: emptySet(),
            availableDays = (doc.get("availableDays") as? List<String>)?.map { AvailabilityDay.valueOf(it) }?.toSet() ?: emptySet(),
            availableTimesOfDay = (doc.get("availableTimesOfDay") as? List<String>)?.map { TimeOfDay.valueOf(it) }?.toSet() ?: emptySet(),
            lessonFormats = (doc.get("lessonFormats") as? List<String>)?.map { LessonFormat.valueOf(it) }?.toSet() ?: emptySet(),
            preferredDurationMinutes = (doc.get("preferredDurationMinutes") as? Number)?.toInt(),
            languages = (doc.get("languages") as? List<Map<String, Any>>)?.map { parseLanguageEntry(it) } ?: emptyList()
        )
    }

    /**
     * Converts a User to a Firestore-compatible map.
     */
    fun toFirestore(user: User): Map<String, Any?> = mapOf(
        "name" to user.name,
        "email" to user.email,
        "teachSkills" to user.teachSkills,
        "learnSkills" to user.learnSkills,
        "points" to user.points,
        "passwordHash" to user.passwordHash,
        "onboardingCompleted" to user.onboardingCompleted,
        "learningGoals" to user.learningGoals.map { it.name },
        "teachingGoals" to user.teachingGoals.map { it.name },
        "availableDays" to user.availableDays.map { it.name },
        "availableTimesOfDay" to user.availableTimesOfDay.map { it.name },
        "lessonFormats" to user.lessonFormats.map { it.name },
        "preferredDurationMinutes" to user.preferredDurationMinutes,
        "languages" to user.languages.map { toMap(it) }
    )

    private fun toMap(entry: LanguageProficiencyEntry): Map<String, Any> = mapOf(
        "languageCode" to entry.languageCode,
        "level" to entry.level.name
    )

    /**
     * Legacy method for backward compatibility.
     */
    @Suppress("UNCHECKED_CAST")
    @Deprecated("Use fromFirestore(DocumentSnapshot) instead", ReplaceWith("fromFirestore(doc)"))
    fun fromDocument(id: String, data: Map<String, Any?>): User {
        return User(
            id = id,
            name = (data["name"] as? String) ?: "",
            email = (data["email"] as? String) ?: "",
            teachSkills = (data["teachSkills"] as? List<String>) ?: emptyList(),
            learnSkills = (data["learnSkills"] as? List<String>) ?: emptyList(),
            points = ((data["points"] as? Number)?.toInt()) ?: 0,
            passwordHash = (data["passwordHash"] as? String) ?: "",
            onboardingCompleted = (data["onboardingCompleted"] as? Boolean) ?: false,
            learningGoals = (data["learningGoals"] as? List<String>)?.map { com.skillx.server.features.onboarding.domain.model.LearningGoal.valueOf(it) }?.toSet() ?: emptySet(),
            teachingGoals = (data["teachingGoals"] as? List<String>)?.map { com.skillx.server.features.onboarding.domain.model.TeachingGoal.valueOf(it) }?.toSet() ?: emptySet(),
            availableDays = (data["availableDays"] as? List<String>)?.map { com.skillx.server.features.onboarding.domain.model.AvailabilityDay.valueOf(it) }?.toSet() ?: emptySet(),
            availableTimesOfDay = (data["availableTimesOfDay"] as? List<String>)?.map { TimeOfDay.valueOf(it) }?.toSet() ?: emptySet(),
            lessonFormats = (data["lessonFormats"] as? List<String>)?.map { com.skillx.server.features.onboarding.domain.model.LessonFormat.valueOf(it) }?.toSet() ?: emptySet(),
            preferredDurationMinutes = (data["preferredDurationMinutes"] as? Number)?.toInt(),
            languages = (data["languages"] as? List<Map<String, Any>>)?.map { parseLanguageEntry(it) } ?: emptyList()
        )
    }

    private fun parseLanguageEntry(map: Map<String, Any>): com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry {
        return com.skillx.server.features.onboarding.domain.model.LanguageProficiencyEntry(
            languageCode = map["languageCode"] as String,
            level = com.skillx.server.features.onboarding.domain.model.CefrLevel.valueOf(map["level"] as String)
        )
    }
}