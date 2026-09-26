package com.skillx.server.features.users.domain.model
data class User(val id: String, val name: String, val email: String, val teachSkills: List<String>, val learnSkills: List<String>, val points: Int)
