package com.skillx.server.core.logging

import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

object RequestLogger {
    private val logger = LoggerFactory.getLogger("RequestLogger")
    fun log(call: RoutingCall) {
        val remoteAddr = "unknown"
        logger.info("${call.request.httpMethod.value} ${call.request.path()} from $remoteAddr")
    }
}