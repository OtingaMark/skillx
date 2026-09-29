package com.skillx.features.matching.data.mapper
import com.skillx.features.matching.data.dto.SkillMatchDto
import com.skillx.features.matching.domain.model.SkillMatch

object SkillMatchMapper {
    fun toDomain(dto: SkillMatchDto): SkillMatch = SkillMatch(uid = dto.uid, name = dto.name, email = dto.email, teachSkills = dto.teachSkills, learnSkills = dto.learnSkills, matchedSkill = dto.matchedSkill)
    fun toDomainList(dtos: List<SkillMatchDto>): List<SkillMatch> = dtos.map { toDomain(it) }
}
