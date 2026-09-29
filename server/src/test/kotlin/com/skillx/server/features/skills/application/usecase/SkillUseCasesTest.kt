package com.skillx.server.features.skills.application.usecase

import com.google.cloud.firestore.Transaction
import com.skillx.server.features.users.domain.model.User
import com.skillx.server.features.users.domain.repository.UserRepository
import com.skillx.server.infrastructure.firestore.FirestoreTransactionRunner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class SkillUseCasesTest {

    private val userId = "user-1"
    private val tx = mockk<Transaction>()
    private val userRepository = mockk<UserRepository>()
    private val transactionRunner = mockk<FirestoreTransactionRunner>().also { runner ->
        coEvery { runner.runTransaction<Any?>(any()) } coAnswers {
            @Suppress("UNCHECKED_CAST")
            val block = firstArg<suspend (Transaction) -> Any?>()
            block(tx)
        }
    }

    private fun user(teachSkills: List<String> = emptyList(), learnSkills: List<String> = emptyList()) =
        User(id = userId, name = "Test", email = "test@example.com", teachSkills = teachSkills, learnSkills = learnSkills, points = 5)

    @Test
    fun `adding a teach skill appends it once`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user()
        coEvery { userRepository.updateSkills(userId, listOf("guitar"), emptyList(), tx) } returns Unit

        AddTeachingSkillUseCase(userRepository, transactionRunner)(userId, "guitar")

        coVerify(exactly = 1) { userRepository.updateSkills(userId, listOf("guitar"), emptyList(), tx) }
    }

    @Test
    fun `adding a teach skill that's already present is a no-op`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user(teachSkills = listOf("guitar"))

        AddTeachingSkillUseCase(userRepository, transactionRunner)(userId, "guitar")

        coVerify(exactly = 0) { userRepository.updateSkills(any(), any(), any(), any()) }
    }

    @Test
    fun `removing a teach skill that's not present is a no-op`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user(teachSkills = listOf("piano"))

        RemoveTeachingSkillUseCase(userRepository, transactionRunner)(userId, "guitar")

        coVerify(exactly = 0) { userRepository.updateSkills(any(), any(), any(), any()) }
    }

    @Test
    fun `removing a present teach skill drops only that skill`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user(teachSkills = listOf("guitar", "piano"))
        coEvery { userRepository.updateSkills(userId, listOf("piano"), emptyList(), tx) } returns Unit

        RemoveTeachingSkillUseCase(userRepository, transactionRunner)(userId, "guitar")

        coVerify(exactly = 1) { userRepository.updateSkills(userId, listOf("piano"), emptyList(), tx) }
    }

    @Test
    fun `adding a learn skill that's already present is a no-op`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user(learnSkills = listOf("french"))

        AddLearningSkillUseCase(userRepository, transactionRunner)(userId, "french")

        coVerify(exactly = 0) { userRepository.updateSkills(any(), any(), any(), any()) }
    }

    @Test
    fun `adding a new learn skill appends it`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user(learnSkills = listOf("french"))
        coEvery { userRepository.updateSkills(userId, emptyList(), listOf("french", "spanish"), tx) } returns Unit

        AddLearningSkillUseCase(userRepository, transactionRunner)(userId, "spanish")

        coVerify(exactly = 1) { userRepository.updateSkills(userId, emptyList(), listOf("french", "spanish"), tx) }
    }

    @Test
    fun `removing a present learn skill drops only that skill`() = runBlocking {
        coEvery { userRepository.findById(userId, tx) } returns user(learnSkills = listOf("french", "spanish"))
        coEvery { userRepository.updateSkills(userId, emptyList(), listOf("spanish"), tx) } returns Unit

        RemoveLearningSkillUseCase(userRepository, transactionRunner)(userId, "french")

        coVerify(exactly = 1) { userRepository.updateSkills(userId, emptyList(), listOf("spanish"), tx) }
    }
}
