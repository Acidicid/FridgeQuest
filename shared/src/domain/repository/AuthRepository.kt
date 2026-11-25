package org.example.project.domain.repository

interface AuthRepository {
    /** Повертає success, якщо вхід вдався. */
    suspend fun signIn(username: String, password: String): Result<Unit>
}
