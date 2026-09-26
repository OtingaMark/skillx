package com.skillx.server.configuration

data class FirebaseConfig(
    val serviceAccountPath: String
) {
    companion object {
        fun fromEnvironment(): FirebaseConfig {
            return FirebaseConfig(
                serviceAccountPath = System.getenv("FIREBASE_SERVICE_ACCOUNT_PATH") ?: "firebase-service-account.json"
            )
        }
    }
}
