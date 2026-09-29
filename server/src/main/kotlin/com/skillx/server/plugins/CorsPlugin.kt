package com.skillx.server.plugins

import com.skillx.server.configuration.AppConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

/**
 * Configures Cross-Origin Resource Sharing (CORS) for the API.
 * Allowed origins are loaded from AppConfig (environment variables).
 */
fun Application.configureCors(appConfig: AppConfig) {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Patch)

        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.Origin)
        allowHeader("X-Requested-With")

        // Configure allowed origins from config
        // In development, allow all; in production, restrict to specific origins
        val allowedOrigins = appConfig.corsAllowedOrigins
        if (allowedOrigins.contains("*")) {
            anyHost()
        } else {
            allowedOrigins.forEach {
                val url = Url(it)
                allowHost(url.host, schemes = listOf(url.protocol.name))
            }
        }

        allowCredentials = true
        maxAgeInSeconds = 3600
    }
}