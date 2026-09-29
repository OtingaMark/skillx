package com.skillx.features.users.data.mapper

import com.skillx.features.users.data.dto.UserProfileDto
import com.skillx.features.users.domain.model.UserProfile

object UserMapper {
    fun toDomain(dto: UserProfileDto): UserProfile {
        return UserProfile(
            id = dto.id,
            name = dto.name,
            email = dto.email,
            teachSkills = dto.teachSkills,
            learnSkills = dto.learnSkills,
            points = dto.points
        )
    }
}
