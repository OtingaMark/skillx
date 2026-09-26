package com.skillx.server.features.ratings.application.usecase
class SubmitRatingUseCase { suspend operator fun invoke(lessonId: String, raterId: String, ratedUserId: String, rating: Int, comment: String) = Unit }
