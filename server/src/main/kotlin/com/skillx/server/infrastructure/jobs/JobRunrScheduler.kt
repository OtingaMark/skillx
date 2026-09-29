package com.skillx.server.infrastructure.jobs

import com.skillx.server.configuration.JobRunrConfig
import org.jobrunr.jobs.lambdas.JobLambda
import org.jobrunr.scheduling.JobScheduler
import org.jobrunr.storage.StorageProvider
import java.time.Instant
import javax.sql.DataSource

class JobRunrScheduler(
    private val config: JobRunrConfig,
    private val storageProvider: StorageProvider,
    private val dataSource: DataSource?
) {
    private val jobScheduler: JobScheduler = JobScheduler(storageProvider)

    fun start() {}

    fun enqueue(job: JobLambda) {
        jobScheduler.enqueue(job)
    }

    fun schedule(delayMs: Long, job: JobLambda) {
        jobScheduler.schedule(Instant.now().plusMillis(delayMs), job)
    }

    fun scheduleRecurring(id: String, cronExpression: String, job: JobLambda) {
        jobScheduler.scheduleRecurrently(id, cronExpression, job)
    }

    fun stop() {
        jobScheduler.shutdown()
    }
}
