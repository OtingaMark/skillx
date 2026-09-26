package com.skillx.server.features.users.application.mapper
object UserResponseMapper { fun toResponse(id: String, name: String, email: String, teachSkills: List<String>, learnSkills: List<String>, points: Int) = mapOf("id" to id, "name" to name, "email" to email, "teachSkills" to teachSkills, "learnSkills" to learnSkills, "points" to points) }
