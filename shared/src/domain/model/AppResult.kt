package org.example.project.domain.model

/**
 * Результат операції: або дані, або зрозуміла помилка.
 * Як посилка з пошти: або всередині товар (Success), або записка "адресата не знайдено" (Error).
 */
sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Error(val error: AppError) : AppResult<Nothing>
}

/** Види помилок, які UI вміє показати людською мовою. */
enum class AppError {
    InvalidCredentials,
    SessionExpired,
    NoInternet,
    Timeout,
    Server,
    Unknown,
}

inline fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> = when (this) {
    is AppResult.Success -> AppResult.Success(transform(data))
    is AppResult.Error -> this
}

inline fun <T> AppResult<T>.onSuccess(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) action(data)
    return this
}

inline fun <T> AppResult<T>.onError(action: (AppError) -> Unit): AppResult<T> {
    if (this is AppResult.Error) action(error)
    return this
}
