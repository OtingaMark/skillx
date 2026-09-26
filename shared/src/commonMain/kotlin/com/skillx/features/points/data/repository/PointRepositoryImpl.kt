package com.skillx.features.points.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.points.data.remote.PointApi
import com.skillx.features.points.data.remote.PointBalanceDto
import com.skillx.features.points.data.remote.PointTransactionDto
import com.skillx.features.points.domain.model.PointBalance
import com.skillx.features.points.domain.model.PointTransaction
import com.skillx.features.points.domain.model.TransactionReason
import com.skillx.features.points.domain.repository.PointRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*

class PointRepositoryImpl(private val api: PointApi) : PointRepository {
    override suspend fun getBalance(): AppResult<PointBalance, AppError> {
        return try {
            val response = api.getBalance()
            if (response.status.isSuccess()) {
                val dto = response.body<PointBalanceDto>()
                AppResult.Success(PointBalance(dto.userId, dto.points))
            } else { val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }; AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err)) }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load balance.")) }
    }
    override suspend fun getTransactionHistory(): AppResult<List<PointTransaction>, AppError> {
        return try {
            val response = api.getHistory()
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<List<PointTransactionDto>>().map {
                    PointTransaction(it.id, it.fromUserId, it.toUserId, it.amount,
                        try { TransactionReason.valueOf(it.reason) } catch (_: Exception) { TransactionReason.LESSON_COMPLETED },
                        it.lessonId, it.timestamp)
                })
            } else { val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }; AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err)) }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load history.")) }
    }
}
