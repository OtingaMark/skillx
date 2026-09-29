package com.skillx.server.features.points.application.usecase

import com.skillx.server.features.points.domain.model.TransactionReason
import com.skillx.server.features.points.domain.repository.PointLedgerRepository
import com.skillx.server.features.points.domain.repository.PointRepository
import com.skillx.server.features.points.domain.service.PointTransferService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TransferPointsUseCaseTest {

    private val pointRepository = mockk<PointRepository>()
    private val ledgerRepository = mockk<PointLedgerRepository>()
    private val service = PointTransferService(pointRepository, ledgerRepository)

    @Test
    fun `transfer succeeds when balance is sufficient`() = runBlocking {
        coEvery { pointRepository.getBalance("from", null) } returns 5
        coEvery { pointRepository.adjustBalance(any(), any(), null) } returns Unit
        coEvery { ledgerRepository.create(any(), null) } returns Unit

        val result = service.transferPoints("from", "to", 1, TransactionReason.LESSON_COMPLETED)

        assertTrue(result)
        coVerify(exactly = 1) { pointRepository.adjustBalance("from", -1, null) }
        coVerify(exactly = 1) { pointRepository.adjustBalance("to", 1, null) }
        coVerify(exactly = 1) { ledgerRepository.create(any(), null) }
    }

    @Test
    fun `transfer fails when balance is insufficient`() = runBlocking {
        coEvery { pointRepository.getBalance("from", null) } returns 0

        val result = service.transferPoints("from", "to", 1, TransactionReason.LESSON_COMPLETED)

        assertFalse(result)
        coVerify(exactly = 0) { pointRepository.adjustBalance(any(), any(), any()) }
        coVerify(exactly = 0) { ledgerRepository.create(any(), any()) }
    }
}
