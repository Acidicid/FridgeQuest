package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.AppResult
import org.example.project.domain.model.PasswordError
import org.example.project.domain.model.User
import org.example.project.domain.model.UsernameError
import org.example.project.domain.repository.AuthRepository

/** Повертає помилку або null, якщо імʼя коректне. */
class ValidateUsernameUseCase {
    operator fun invoke(username: String): UsernameError? {
        val value = username.trim()
        return when {
            value.isEmpty() -> UsernameError.Empty
            value.length < MIN_LENGTH -> UsernameError.TooShort
            value.length > MAX_LENGTH -> UsernameError.TooLong
            !ALLOWED.matches(value) -> UsernameError.InvalidChars
            else -> null
        }
    }

    private companion object {
        const val MIN_LENGTH = 3
        const val MAX_LENGTH = 20
        val ALLOWED = Regex("^[A-Za-z0-9_.]+$")
    }
}

/**
 * Повертає помилку або null, якщо пароль коректний.
 * Правила мʼякші, ніж у лабі 5: тестові паролі DummyJSON (напр. "emilyspass") не мають цифр,
 * а перевіряти "чи правильний пароль" має сервер, а не клієнт.
 */
class ValidatePasswordUseCase {
    operator fun invoke(password: String): PasswordError? = when {
        password.isEmpty() -> PasswordError.Empty
        password.length < MIN_LENGTH -> PasswordError.TooShort
        else -> null
    }

    private companion object {
        const val MIN_LENGTH = 6
    }
}

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): AppResult<Unit> =
        repository.login(username.trim(), password)
}

class LogoutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logout()
}

class IsUserLoggedInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Boolean = repository.isLoggedIn()
}

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): AppResult<User> = repository.getCurrentUser()
}

class ObserveSessionExpiredUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<Unit> = repository.sessionExpired
}
