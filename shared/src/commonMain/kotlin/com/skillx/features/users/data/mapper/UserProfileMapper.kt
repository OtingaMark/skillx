package com.skillx.features.users.data.mapper
import com.skillx.features.users.data.dto.UserProfileDto
import com.skillx.features.users.domain.model.UserProfile
object UserProfileMapper {
    fun toDomain(dto: UserProfileDto): UserProfile = UserProfile(
        id = dto.id,
        name = dto.name,
        email = dto.email,
        teachSkills = dto.teachSkills,
        learnSkills = dto.learnSkills,
        points = dto.points,
        onboardingCompleted = dto.onboardingCompleted,
        learningGoals = dto.learningGoals,
        teachingGoals = dto.teachingGoals,
        availableDays = dto.availableDays,
        availableTimesOfDay = dto.availableTimesOfDay,
        lessonFormats = dto.lessonFormats,
        preferredDurationMinutes = dto.preferredDurationMinutes,
        languages = dto.languages
    )
    fun toDto(domain: UserProfile): UserProfileDto = UserProfileDto(
        id = domain.id,
        name = domain.name,
        email = domain.email,
        teachSkills = domain.teachSkills,
        learnSkills = domain.learnSkills,
        points = domain.points,
        onboardingCompleted = domain.onboardingCompleted,
        learningGoals = domain.learningGoals,
        teachingGoals = domain.teachingGoals,
        availableDays = domain.availableDays,
        availableTimesOfDay = domain.availableTimesOfDay,
        lessonFormats = domain.lessonFormats,
        preferredDurationMinutes = domain.preferredDurationMinutes,
        languages = domain.languages
    )
}
