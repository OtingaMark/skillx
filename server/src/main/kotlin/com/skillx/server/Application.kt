package com.skillx.server

import com.skillx.server.plugins.*
import com.skillx.server.di.serverModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.ktor.plugin.Koin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(serverModule)
    }
    configureContentNegotiation()
    configureAuthentication()
    configureStatusPages()
    configureCors()
    configureRateLimit()
    configureMonitoring()
    configureWebSockets()
    configureRouting()
}
