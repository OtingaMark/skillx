package com.skillx.server

import com.skillx.server.plugins.*
import com.skillx.server.di.serverModule
import com.skillx.server.configuration.AppConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin
import org.koin.ktor.ext.get

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(serverModule)
    }

    val appConfig = get<AppConfig>()

    configureContentNegotiation()
    configureAuthentication()
    configureStatusPages()
    configureCors(appConfig)
    configureRateLimit()
    configureMonitoring()
    configureWebSockets()
    configureRouting()
}