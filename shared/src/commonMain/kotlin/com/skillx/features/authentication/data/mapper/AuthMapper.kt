package com.skillx.features.authentication.data.mapper

import com.skillx.features.authentication.data.dto.AuthResponseDto
import com.skillx.features.authentication.domain.model.AuthSession

/**
 * Maps between network DTOs and domain models for authentication.
 */
object AuthMapper {

    fun toDomain(dto: AuthResponseDto): AuthSession {
        return AuthSession(
            userId = dto.userId,
            email = dto.email,
            token = dto.token,
            refreshToken = dto.refreshToken
        )
    }
}
