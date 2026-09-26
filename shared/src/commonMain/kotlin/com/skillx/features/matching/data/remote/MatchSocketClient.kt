package com.skillx.features.matching.data.remote
import com.skillx.features.matching.domain.model.SkillMatch
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json

/**
 * WebSocket client for live match streaming.
 */
class MatchSocketClient(private val client: HttpClient) {
    fun observeMatches(): Flow<List<SkillMatch>> = flow {
        // Placeholder — real implementation uses client.webSocket()
        emit(emptyList())
    }
}
