package com.skillx.server.features.users.application.usecase
class UpdateUserProfileUseCase { suspend operator fun invoke(userId: String, name: String, teachSkills: List<String>, learnSkills: List<String>) = Unit }
