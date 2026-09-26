package com.skillx.server.features.authentication.application.mapper
object AuthResponseMapper { fun toResponse(userId: String, email: String, token: String, refreshToken: String) = mapOf("userId" to userId, "email" to email, "token" to token, "refreshToken" to refreshToken) }
