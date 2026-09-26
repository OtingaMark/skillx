package com.skillx.features.skills.domain.usecase

import com.skillx.core.error.AppError
import com.skillx.core.error.ValidationError
import com.skillx.core.result.AppResult
import com.skillx.features.skills.domain.repository.SkillRepository

class AddTeachingSkillUseCase(private val repository: SkillRepository) {
    suspend operator fun invoke(skill: String): AppResult<List<String>, AppError> {
        if (skill.isBlank()) return AppResult.Error(ValidationError.Custom("Skill name cannot be empty."))
        return repository.addTeachingSkill(skill.trim())
    }
}

class RemoveTeachingSkillUseCase(private val repository: SkillRepository) {
    suspend operator fun invoke(skill: String): AppResult<List<String>, AppError> {
        return repository.removeTeachingSkill(skill)
    }
}

class AddLearningSkillUseCase(private val repository: SkillRepository) {
    suspend operator fun invoke(skill: String): AppResult<List<String>, AppError> {
        if (skill.isBlank()) return AppResult.Error(ValidationError.Custom("Skill name cannot be empty."))
        return repository.addLearningSkill(skill.trim())
    }
}

class RemoveLearningSkillUseCase(private val repository: SkillRepository) {
    suspend operator fun invoke(skill: String): AppResult<List<String>, AppError> {
        return repository.removeLearningSkill(skill)
    }
}
