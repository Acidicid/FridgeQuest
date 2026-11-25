package org.example.project.data.network

import co.touchlab.kermit.Logger
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlin.coroutines.cancellation.CancellationException
import org.example.project.domain.model.AppError
import org.example.project.domain.model.AppResult

/** Як перетворити HTTP-код помилки на [AppError] за замовчуванням. */
internal fun defaultHttpError(status: Int): AppError = when (status) {
    401, 403 -> AppError.SessionExpired
    in 500..599 -> AppError.Server
    else -> AppError.Unknown
}

/**
 * Обгортка над будь-яким запитом: ловить винятки й HTTP-помилки
 * і повертає [AppResult] замість того, щоб "падати".
 */
internal suspend inline fun <reified T> safeRequest(
    noinline mapHttpError: (status: Int) -> AppError = ::defaultHttpError,
    request: () -> HttpResponse,
): AppResult<T> {
    val response = try {
        request()
    } catch (e: CancellationException) {
        throw e // скасування корутини не можна "ковтати"
    } catch (e: HttpRequestTimeoutException) {
        Logger.w("Network") { "Timeout: ${e.message}" }
        return AppResult.Error(AppError.Timeout)
    } catch (e: Exception) {
        Logger.w("Network") { "No connection: ${e.message}" }
        return AppResult.Error(AppError.NoInternet)
    }

    val status = response.status.value
    if (status !in 200..299) {
        // bodyAsText() - suspend, тому читаємо ДО логера (його лямбда не suspend)
        val errorBody = try {
            response.bodyAsText()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ""
        }
        Logger.w("Network") { "HTTP $status: $errorBody" }
        return AppResult.Error(mapHttpError(status))
    }

    return try {
        AppResult.Success(response.body<T>())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Logger.e("Network") { "Cannot parse response: ${e.message}" }
        AppResult.Error(AppError.Unknown)
    }
}
