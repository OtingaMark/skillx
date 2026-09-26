package com.skillx.features.users.data.mapper
import com.skillx.features.users.data.dto.UserProfileDto
import com.skillx.features.users.domain.model.UserProfile
object UserProfileMapper {
    fun toDomain(dto: UserProfileDto): UserProfile = UserProfile(id = dto.id, name = dto.name, email = dto.email, teachSkills = dto.teachSkills, learnSkills = dto.learnSkills, points = dto.points)
    fun toDto(domain: UserProfile): UserProfileDto = UserProfileDto(id = domain.id, name = domain.name, email = domain.email, teachSkills = domain.teachSkills, learnSkills = domain.learnSkills, points = domain.points)
}
