package com.skillx.server.features.authentication.application.usecase
class RegisterUserUseCase { suspend operator fun invoke(name: String, email: String, passwordHash: String): String { return "" /* delegated to route for now */ } }
