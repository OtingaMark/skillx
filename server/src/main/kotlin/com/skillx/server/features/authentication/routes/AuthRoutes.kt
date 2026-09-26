package com.skillx.server.features.authentication.routes

import com.skillx.server.infrastructure.authentication.JwtTokenService
import com.skillx.server.infrastructure.authentication.PasswordHasher
import com.skillx.server.infrastructure.firestore.FirestoreClientProvider
import com.skillx.server.core.exceptions.AuthenticationException
import com.skillx.server.core.exceptions.ValidationException
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable data class RegisterRequest(val name: String, val email: String, val password: String)
@Serializable data class LoginRequest(val email: String, val password: String)
@Serializable data class AuthResponse(val userId: String, val email: String, val token: String, val refreshToken: String)

fun Route.authRoutes() {
    val jwtService by inject<JwtTokenService>()
    val firestoreProvider by inject<FirestoreClientProvider>()

    route("/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            if (request.name.isBlank() || request.email.isBlank() || request.password.length < 6) {
                throw ValidationException("INVALID_INPUT", "Name, email, and password (min 6 chars) are required.")
            }

            val db = firestoreProvider.getFirestore()
            val passwordHash = PasswordHasher.hash(request.password)
            val userId = java.util.UUID.randomUUID().toString()

            val userData = mapOf(
                "name" to request.name.trim(),
                "email" to request.email.trim().lowercase(),
                "passwordHash" to passwordHash,
                "teachSkills" to emptyList<String>(),
                "learnSkills" to emptyList<String>(),
                "points" to 5
            )
            db.collection("users").document(userId).set(userData).get()

            val token = jwtService.generateToken(userId, request.email)
            val refreshToken = jwtService.generateRefreshToken(userId)
            call.respond(HttpStatusCode.Created, AuthResponse(userId, request.email, token, refreshToken))
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val db = firestoreProvider.getFirestore()

            val snapshot = db.collection("users").whereEqualTo("email", request.email.trim().lowercase()).get().get()
            if (snapshot.isEmpty) throw AuthenticationException("Invalid email or password.")

            val doc = snapshot.documents.first()
            val hash = doc.getString("passwordHash") ?: throw AuthenticationException("Invalid email or password.")
            if (!PasswordHasher.verify(request.password, hash)) throw AuthenticationException("Invalid email or password.")

            val userId = doc.id
            val token = jwtService.generateToken(userId, request.email)
            val refreshToken = jwtService.generateRefreshToken(userId)
            call.respond(AuthResponse(userId, request.email, token, refreshToken))
        }

        post("/logout") { call.respond(HttpStatusCode.OK, mapOf("message" to "Logged out.")) }
    }
}
