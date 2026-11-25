package org.example.project.domain.usecase

import org.example.project.domain.model.PasswordError
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

/** Повертає помилку або null, якщо пароль коректний. */
class ValidatePasswordUseCase {
    operator fun invoke(password: String): PasswordError? = when {
        password.isEmpty() -> PasswordError.Empty
        password.length < MIN_LENGTH -> PasswordError.TooShort
        password.none { it.isDigit() } -> PasswordError.NoDigit
        password.none { it.isLetter() } -> PasswordError.NoLetter
        else -> null
    }

    private companion object {
        const val MIN_LENGTH = 8
    }
}

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Result<Unit> =
        repository.signIn(username.trim(), password)
}
