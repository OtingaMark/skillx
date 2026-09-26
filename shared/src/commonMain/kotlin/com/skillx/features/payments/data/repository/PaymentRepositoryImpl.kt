package com.skillx.features.payments.data.repository

import com.skillx.core.error.AppError
import com.skillx.core.error.NetworkError
import com.skillx.core.result.AppResult
import com.skillx.features.payments.data.remote.PaymentApi
import com.skillx.features.payments.data.remote.PointPackageDto
import com.skillx.features.payments.data.remote.VerifyPurchaseRequestDto
import com.skillx.features.payments.data.remote.VerifyPurchaseResponseDto
import com.skillx.features.payments.domain.model.PointPackage
import com.skillx.features.payments.domain.repository.PaymentRepository
import com.skillx.network.error.ApiErrorMapper
import com.skillx.network.error.ApiErrorResponse
import io.ktor.client.call.*
import io.ktor.http.*

class PaymentRepositoryImpl(private val api: PaymentApi) : PaymentRepository {
    override suspend fun getPointPackages(): AppResult<List<PointPackage>, AppError> {
        return try {
            val response = api.getPackages()
            if (response.status.isSuccess()) {
                AppResult.Success(response.body<List<PointPackageDto>>().map { PointPackage(it.productId, it.points, it.formattedPrice) })
            } else { val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }; AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err)) }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to load packages.")) }
    }
    override suspend fun verifyPurchase(productId: String, transactionId: String, receipt: String): AppResult<Int, AppError> {
        return try {
            val response = api.verifyPurchase(VerifyPurchaseRequestDto(productId, transactionId, receipt))
            if (response.status.isSuccess()) AppResult.Success(response.body<VerifyPurchaseResponseDto>().pointsCredited)
            else { val err = try { response.body<ApiErrorResponse>() } catch (_: Exception) { null }; AppResult.Error(ApiErrorMapper.fromHttpStatus(response.status.value, err)) }
        } catch (e: Exception) { AppResult.Error(NetworkError.Unknown(e.message ?: "Failed to verify purchase.")) }
    }
}
