package com.skillx.features.authentication.data.mapper
import com.skillx.features.authentication.data.dto.AuthResponseDto
import com.skillx.features.authentication.domain.model.AuthSession

object AuthResponseMapper {
    fun toDomain(dto: AuthResponseDto): AuthSession = AuthSession(userId = dto.userId, email = dto.email, token = dto.token, refreshToken = dto.refreshToken)
}
