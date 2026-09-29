package com.skillx.server.features.users.interfaces.http.response
import com.skillx.server.features.onboarding.domain.model.AvailabilityDay
import com.skillx.server.features.onboarding.domain.model.LearningGoal
import com.skillx.server.features.onboarding.domain.model.LessonFormat
import com.skillx.server.features.onboarding.domain.model.TeachingGoal
import com.skillx.server.features.onboarding.domain.model.TimeOfDay
import com.skillx.server.features.onboarding.interfaces.http.response.LanguageProficiencyEntryResponse
import kotlinx.serialization.Serializable

@Serializable data class UserResponse(
    val id: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val points: Int,
    val onboardingCompleted: Boolean = false,
    val learningGoals: Set<LearningGoal> = emptySet(),
    val teachingGoals: Set<TeachingGoal> = emptySet(),
    val availableDays: Set<AvailabilityDay> = emptySet(),
    val availableTimesOfDay: Set<TimeOfDay> = emptySet(),
    val lessonFormats: Set<LessonFormat> = emptySet(),
    val preferredDurationMinutes: Int? = null,
    val languages: List<LanguageProficiencyEntryResponse> = emptyList()
)
