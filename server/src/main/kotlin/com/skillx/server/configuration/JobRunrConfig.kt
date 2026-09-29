package com.skillx.server.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

/**
 * JobRunr configuration loaded from environment variables.
 * Supports in-memory storage (dev) and MySQL/TiDB storage (production).
 */
data class JobRunrConfig(
    val storageType: String,           // "in-memory" or "mysql"
    val pollInterval: Long,            // Poll interval in seconds
    val datasourceUrl: String? = null, // JDBC URL for MySQL/TiDB
    val datasourceUser: String? = null,
    val datasourcePassword: String? = null,
    val datasourceMaxPoolSize: Int = 10
) {
    companion object {
        fun fromEnvironment(): JobRunrConfig {
            val storageType = System.getenv("JOBRUNR_STORAGE") ?: "in-memory"
            return when (storageType) {
                "mysql" -> JobRunrConfig(
                    storageType = storageType,
                    pollInterval = (System.getenv("JOBRUNR_POLL_INTERVAL") ?: "15").toLong(),
                    datasourceUrl = System.getenv("JOBRUNR_DATASOURCE_URL"),
                    datasourceUser = System.getenv("JOBRUNR_DATASOURCE_USER"),
                    datasourcePassword = System.getenv("JOBRUNR_DATASOURCE_PASSWORD"),
                    datasourceMaxPoolSize = (System.getenv("JOBRUNR_DATASOURCE_MAX_POOL") ?: "10").toInt()
                )
                else -> JobRunrConfig(
                    storageType = storageType,
                    pollInterval = (System.getenv("JOBRUNR_POLL_INTERVAL") ?: "15").toLong()
                )
            }
        }
    }

    /**
     * Creates a HikariCP DataSource for MySQL/TiDB storage.
     * Returns null for in-memory storage.
     */
    fun createDataSource(): DataSource? = if (storageType == "mysql") {
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = datasourceUrl ?: throw IllegalStateException("JOBRUNR_DATASOURCE_URL not set")
            username = datasourceUser ?: throw IllegalStateException("JOBRUNR_DATASOURCE_USER not set")
            password = datasourcePassword ?: throw IllegalStateException("JOBRUNR_DATASOURCE_PASSWORD not set")
            maximumPoolSize = datasourceMaxPoolSize
            isAutoCommit = true
            connectionTimeout = 30000L
            idleTimeout = 600000L
            maxLifetime = 1800000L
        }
        HikariDataSource(hikariConfig)
    } else null
}