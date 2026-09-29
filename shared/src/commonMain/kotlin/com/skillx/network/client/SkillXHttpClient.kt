package com.skillx.network.client

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.network.authentication.AuthTokenProvider
import com.skillx.network.configuration.ApiConfiguration
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import com.skillx.network.serialization.NetworkJson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

/**
 * Creates and configures the Ktor HttpClient with auth, content negotiation, and logging.
 */
class SkillXHttpClientFactory(
    private val apiConfiguration: ApiConfiguration,
    private val tokenProvider: AuthTokenProvider,
    private val engine: HttpClientEngine? = null
) {
    fun create(): HttpClient {
        val builder: HttpClientConfig<*>.() -> Unit = {
            install(ContentNegotiation) {
                json(NetworkJson)
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val access = tokenProvider.getAccessToken()
                        val refresh = tokenProvider.getRefreshToken()
                        if (access != null && refresh != null) {
                            BearerTokens(access, refresh)
                        } else null
                    }

                    refreshTokens {
                        // Token refresh will be handled by server refresh endpoint
                        null
                    }
                }
            }

            install(Logging) {
                level = LogLevel.HEADERS
            }

            install(WebSockets)

            install(HttpTimeout) {
                connectTimeoutMillis = apiConfiguration.connectTimeoutMs
                requestTimeoutMillis = apiConfiguration.requestTimeoutMs
                socketTimeoutMillis = apiConfiguration.socketTimeoutMs
            }

            defaultRequest {
                url(apiConfiguration.baseUrl)
                contentType(ContentType.Application.Json)
            }
        }

        return if (engine != null) {
            HttpClient(engine) { builder() }
        } else {
            HttpClient { builder() }
        }
    }
}

/**
 * Extension to safely execute API calls and map responses to AppResult.
 */
suspend inline fun <reified T> HttpClient.safeApiCall(
    block: HttpClient.() -> HttpResponse
): AppResult<T, AppError> {
    return try {
        val response = block()
        if (response.status.isSuccess()) {
            AppResult.Success(response.body<T>())
        } else {
            val errorBody = try {
                response.body<ApiErrorResponse>()
            } catch (_: Exception) {
                null
            }
            AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, errorBody))
        }
    } catch (e: HttpRequestTimeoutException) {
        AppResult.Error(NetworkError.Timeout)
    } catch (e: Exception) {
        AppResult.Error(NetworkError.Unknown(e.message ?: "An unexpected error occurred."))
    }
}
