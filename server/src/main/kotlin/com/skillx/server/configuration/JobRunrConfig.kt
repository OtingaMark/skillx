package com.skillx.server.configuration
data class JobRunrConfig(val storageType: String, val pollInterval: Long) {
    companion object { fun fromEnvironment() = JobRunrConfig(System.getenv("JOBRUNR_STORAGE") ?: "in-memory", (System.getenv("JOBRUNR_POLL_INTERVAL") ?: "15").toLong()) }
}
