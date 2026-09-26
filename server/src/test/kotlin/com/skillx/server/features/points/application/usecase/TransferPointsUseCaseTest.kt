package com.skillx.server.features.points.application.usecase

import kotlin.test.Test
import kotlin.test.assertTrue

class TransferPointsUseCaseTest {
    @Test fun transferReducesFromBalance() {
        val service = com.skillx.server.features.points.domain.service.PointTransferService()
        assertTrue(service.validateTransfer(5, 1))
    }
    @Test fun transferFailsWithInsufficientPoints() {
        val service = com.skillx.server.features.points.domain.service.PointTransferService()
        assertTrue(!service.validateTransfer(0, 1))
    }
}
