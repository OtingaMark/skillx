package com.skillx.features.users.domain.model

/**
 * Domain model for a user's profile.
 * Extracted from the Firestore 'users' collection document shape.
 * Pure Kotlin — no Firebase, Compose, or Ktor imports.
 */
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val teachSkills: List<String>,
    val learnSkills: List<String>,
    val points: Int
)
