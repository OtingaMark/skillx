package com.skillx.server.features.skills.application.usecase

import com.skillx.server.core.exceptions.NotFoundException
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner

class RemoveLearningSkillUseCase(
    private val userRepository: UserRepository,
    private val transactionRunner: FirestoreTransactionRunner
) {
    suspend operator fun invoke(userId: String, skill: String) {
        transactionRunner.runTransaction { tx ->
            val user = userRepository.findById(userId, tx) ?: throw NotFoundException("User not found.")
            if (skill !in user.learnSkills) return@runTransaction
            userRepository.updateSkills(userId, user.teachSkills, user.learnSkills - skill, tx)
        }
    }
}
