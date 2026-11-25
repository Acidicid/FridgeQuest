package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.AppResult
import org.example.project.domain.model.User

interface AuthRepository {
    /** Логін + збереження токенів у захищене сховище. */
    suspend fun login(username: String, password: String): AppResult<Unit>

    /** Запит, що потребує токена (GET auth/me). Сам оновить токен, якщо той прострочився. */
    suspend fun getCurrentUser(): AppResult<User>

    suspend fun logout()

    /** Ознака авторизованого користувача: чи є збережені токени. */
    suspend fun isLoggedIn(): Boolean

    /** Подія: refresh-токен теж протух -> користувача треба повернути на логін. */
    val sessionExpired: Flow<Unit>
}
