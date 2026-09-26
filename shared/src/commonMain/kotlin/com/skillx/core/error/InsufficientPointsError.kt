package com.skillx.core.error

/**
 * Insufficient points error: user cannot afford the operation.
 */
data class InsufficientPointsError(
    val available: Int = 0,
    val required: Int = 1,
    override val message: String = "You do not have enough SkillX points. Available: $available, required: $required."
) : AppError
