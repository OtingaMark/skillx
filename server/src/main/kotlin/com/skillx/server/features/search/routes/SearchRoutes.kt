package com.skillx.server.features.search.routes

import com.skillx.server.core.extensions.userId
import com.skillx.server.features.search.application.usecase.LoadPopularSkillsUseCase
import com.skillx.server.features.search.application.usecase.SearchPeopleUseCase
import com.skillx.server.features.search.application.usecase.SearchSkillsUseCase
import com.skillx.server.features.search.interfaces.http.response.toResponse
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Discovery routes. Must be registered inside the authenticated route block.
 *  GET /skills/search?query=&limit=   skill catalog search (the client's SkillApi already calls this)
 *  GET /skills/popular?limit=         most-taught skills
 *  GET /search/people?query=&limit=   people by name or skill
 */
fun Route.searchRoutes() {
    val searchSkills by inject<SearchSkillsUseCase>()
    val loadPopularSkills by inject<LoadPopularSkillsUseCase>()
    val searchPeople by inject<SearchPeopleUseCase>()

    get("/skills/search") {
        val query = call.request.queryParameters["query"].orEmpty()
        val limit = call.request.queryParameters["limit"]?.toIntOrNull()
        call.respond(searchSkills(query, limit).map { it.toResponse() })
    }

    get("/skills/popular") {
        val limit = call.request.queryParameters["limit"]?.toIntOrNull()
        call.respond(loadPopularSkills(limit).map { it.toResponse() })
    }

    get("/search/people") {
        val query = call.request.queryParameters["query"].orEmpty()
        val limit = call.request.queryParameters["limit"]?.toIntOrNull()
        call.respond(searchPeople(call.userId(), query, limit).map { it.toResponse() })
    }
}
