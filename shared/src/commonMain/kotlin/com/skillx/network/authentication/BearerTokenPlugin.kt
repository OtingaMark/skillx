package com.skillx.network.authentication

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Ktor client plugin that attaches the Bearer token from AuthTokenProvider
 * to every outgoing HTTP request's Authorization header.
 */
class BearerTokenPlugin(private val tokenProvider: AuthTokenProvider) {

    companion object {
        fun install(client: HttpClientConfig<*>, tokenProvider: AuthTokenProvider) {
            client.install(HttpSend) {
                intercept { request ->
                    val token = tokenProvider.getAccessToken()
                    if (token != null) {
                        request.headers {
                            append(HttpHeaders.Authorization, "Bearer $token")
                        }
                    }
                    execute(request)
                }
            }
        }
    }
}
