plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jvm)
}

group = "com.skillx.server"
version = "0.0.1"

application {
    mainClass.set("com.skillx.server.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

configurations.all {
    resolutionStrategy {
        force("com.google.cloud:google-cloud-firestore:3.22.0")
    }
}

dependencies {
    // Ktor Server
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.cors)
    implementation(libs.ktor.server.websockets)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.server.rate.limit)
    implementation(libs.ktor.server.config.yaml)

    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.guava)

    // DI
    implementation(libs.koin.core)
    implementation(libs.koin.ktor)

    // Firebase Admin
    implementation(libs.firebase.admin)

    // Google Cloud Firestore (server-side, has Kotlin coroutines support)
    implementation("com.google.cloud:google-cloud-firestore:3.22.0")

    // Jobs
    implementation(libs.jobrunr)

    // Security
    implementation(libs.bcrypt)
    implementation(libs.argon2)
    implementation(libs.stripe)
    implementation(libs.jwks.rsa)
    implementation(libs.angus.mail)

    // Database
    implementation(libs.hikaricp)

    // Logging
    implementation(libs.logback.classic)

    // Testing
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.koin.test)
    testImplementation(libs.mockk)
}