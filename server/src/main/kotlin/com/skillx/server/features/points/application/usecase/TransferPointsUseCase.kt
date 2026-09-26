package com.skillx.server.features.points.application.usecase
class TransferPointsUseCase { suspend operator fun invoke(fromUserId: String, toUserId: String, amount: Int) = Unit }
