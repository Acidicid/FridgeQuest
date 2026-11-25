package org.example.project.data.repository

import com.russhwolf.settings.Settings
import kotlinx.coroutines.delay
import org.example.project.domain.repository.AuthRepository

/**
 * Поки що бекенду немає - імітуємо запит до сервера (затримка) і запамʼятовуємо імʼя.
 * Коли зʼявиться справжній API, достатньо замінити цей клас у Koin-модулі.
 */
class FakeAuthRepository(private val settings: Settings) : AuthRepository {

    override suspend fun signIn(username: String, password: String): Result<Unit> {
        delay(800)
        settings.putString(KEY_LAST_USERNAME, username)
        return Result.success(Unit)
    }

    private companion object {
        const val KEY_LAST_USERNAME = "last_username"
    }
}
