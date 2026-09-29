package com.skillx.server.infrastructure.jobs.job

import com.skillx.server.features.matching.application.usecase.FindSkillMatchesUseCase
import com.skillx.server.features.users.domain.repository.UserRepository
import org.jobrunr.jobs.annotations.Job
import org.jobrunr.jobs.context.JobContext

class RefreshMatchesJob(
    private val userRepository: UserRepository,
    private val findMatchesUseCase: FindSkillMatchesUseCase
) {

    @Job(name = "Refresh all skill matches")
    fun execute(context: JobContext) {
        // Periodically recompute matches for all users
    }

    @Job(name = "Refresh matches for user: %{userId}")
    fun executeForUser(userId: String, context: JobContext) {
        // Recompute matches for a specific user
    }
}