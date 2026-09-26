package com.skillx.features.skills.domain.repository

import com.skillx.core.error.AppError
import com.skillx.core.result.AppResult

/**
 * Skill repository interface.
 * Defines operations for managing user teaching and learning skills.
 */
interface SkillRepository {
    suspend fun addTeachingSkill(skill: String): AppResult<List<String>, AppError>
    suspend fun removeTeachingSkill(skill: String): AppResult<List<String>, AppError>
    suspend fun addLearningSkill(skill: String): AppResult<List<String>, AppError>
    suspend fun removeLearningSkill(skill: String): AppResult<List<String>, AppError>
    suspend fun getTeachingSkills(): AppResult<List<String>, AppError>
    suspend fun getLearningSkills(): AppResult<List<String>, AppError>
    suspend fun saveSkills(
        teachSkills: List<String>,
        learnSkills: List<String>
    ): AppResult<Unit, AppError>
}
