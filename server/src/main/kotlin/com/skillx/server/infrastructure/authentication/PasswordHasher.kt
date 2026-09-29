package com.skillx.server.infrastructure.authentication

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory
import at.favre.lib.crypto.bcrypt.BCrypt

/**
 * Password hashing utility with configurable algorithm.
 * Defaults to Argon2id (recommended by OWASP) with bcrypt fallback for legacy hashes.
 * Algorithm is determined by the hash prefix: $argon2id$ for Argon2, $2a$/$2b$/$2y$ for bcrypt.
 */
object PasswordHasher {

    // Argon2id parameters (OWASP recommended minimums as of 2024)
    private const val ARGON2_ITERATIONS = 3
    private const val ARGON2_MEMORY_KIB = 65536 // 64 MB
    private const val ARGON2_PARALLELISM = 4

    // Bcrypt cost factor
    private const val BCRYPT_COST = 12

    private val argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id)

    /**
     * Hashes a password using the configured algorithm (default: Argon2id).
     * @param password The plain-text password to hash
     * @return The hashed password with algorithm prefix for later verification
     */
    fun hash(password: String): String {
        // Default to Argon2id for new passwords
        return hashWithArgon2(password)
    }

    /**
     * Hashes a password using Argon2id.
     * @param password The plain-text password to hash
     * @return Argon2id hash with parameters encoded
     */
    fun hashWithArgon2(password: String): String {
        return argon2.hash(ARGON2_ITERATIONS, ARGON2_MEMORY_KIB, ARGON2_PARALLELISM, password.toCharArray())
    }

    /**
     * Hashes a password using bcrypt (for compatibility/migration).
     * @param password The plain-text password to hash
     * @return Bcrypt hash
     */
    fun hashWithBcrypt(password: String): String {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray())
    }

    /**
     * Verifies a password against a hash.
     * Automatically detects the algorithm from the hash prefix.
     * @param password The plain-text password to verify
     * @param hash The stored hash (with algorithm prefix)
     * @return true if the password matches, false otherwise
     */
    fun verify(password: String, hash: String): Boolean {
        return when {
            hash.startsWith("\$argon2id\$") -> verifyArgon2(password, hash)
            hash.startsWith("\$2a\$") || hash.startsWith("\$2b\$") || hash.startsWith("\$2y\$") -> verifyBcrypt(password, hash)
            else -> {
                // Legacy/unknown format - assume bcrypt for backward compatibility
                verifyBcrypt(password, hash)
            }
        }
    }

    /**
     * Verifies a password against an Argon2id hash.
     */
    private fun verifyArgon2(password: String, hash: String): Boolean {
        return try {
            argon2.verify(hash, password.toCharArray())
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Verifies a password against a bcrypt hash.
     */
    private fun verifyBcrypt(password: String, hash: String): Boolean {
        return try {
            BCrypt.verifyer().verify(password.toCharArray(), hash).verified
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks if a hash needs rehashing (e.g., old algorithm or weak parameters).
     * @param hash The stored hash
     * @return true if the password should be rehashed on next login
     */
    fun needsRehash(hash: String): Boolean {
        return when {
            hash.startsWith("\$argon2id\$") -> {
                // Check if parameters meet current minimums
                // Argon2 hash format: $argon2id$v=19$m=65536,t=3,p=4$salt$hash
                // For simplicity, we assume current params are fine
                false
            }
            hash.startsWith("\$2a\$") || hash.startsWith("\$2b\$") || hash.startsWith("\$2y\$") -> {
                // Extract cost factor from bcrypt hash: $2a$12$...
                val parts = hash.split('$')
                if (parts.size >= 3) {
                    val cost = parts[2].toIntOrNull() ?: BCRYPT_COST
                    cost < BCRYPT_COST
                } else {
                    true
                }
            }
            else -> true // Unknown format, rehash
        }
    }

    /**
     * Rehashes a password with the current default algorithm (Argon2id).
     * Call this after successful verification if needsRehash returns true.
     * @param password The plain-text password
     * @return New Argon2id hash
     */
    fun rehash(password: String): String = hashWithArgon2(password)
}