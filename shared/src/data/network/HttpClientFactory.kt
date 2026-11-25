package org.example.project.data.network

import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.serialization.json.Json
import org.example.project.data.auth.model.RefreshRequest
import org.example.project.data.auth.model.RefreshResponse
import org.example.project.data.security.AuthTokens
import org.example.project.data.security.TokenStorage
import org.example.project.data.session.SessionEvents

/**
 * Єдиний HttpClient на весь застосунок. Рушій (engine) Ktor підбирає сам:
 * Android - ktor-client-android, Desktop - CIO, Web - JS fetch.
 */
internal fun createHttpClient(
    tokenStorage: TokenStorage,
    sessionEvents: SessionEvents,
): HttpClient = HttpClient {
    // Помилки 4xx/5xx розбираємо самі в safeRequest, тому не кидаємо винятки
    expectSuccess = false

    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
        connectTimeoutMillis = 10_000
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true // сервер шле більше полів, ніж нам треба
            // без цього поля зі значенням за замовчуванням (expiresInMins = 1) НЕ потрапляють у JSON
            encodeDefaults = true
            isLenient = true
        })
    }

    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) = KermitLogger.d("Ktor") { message }
        }
        // токени в логи не пишемо
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }

    /*
     * Автоматичне оновлення токенів.
     * Аналогія: access-токен - перепустка на день, refresh-токен - паспорт.
     * Охоронець (сервер) каже 401 "перепустка прострочена" -> Ktor сам іде на прохідну
     * (/auth/refresh) з паспортом, отримує нову перепустку й повторює вихідний запит.
     * Користувач нічого не помічає.
     */
    install(Auth) {
        bearer {
            loadTokens {
                tokenStorage.getTokens()?.let { BearerTokens(it.accessToken, it.refreshToken) }
            }

            refreshTokens {
                val refreshToken = oldTokens?.refreshToken ?: tokenStorage.getTokens()?.refreshToken
                if (refreshToken == null) {
                    sessionEvents.notifyExpired()
                    return@refreshTokens null
                }

                val response = try {
                    client.post("$BASE_URL$REFRESH_PATH") {
                        markAsRefreshTokenRequest() // щоб цей запит сам не потрапив у цикл оновлення
                        contentType(ContentType.Application.Json)
                        setBody(RefreshRequest(refreshToken))
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    KermitLogger.w("Auth") { "Refresh failed (network): ${e.message}" }
                    return@refreshTokens null // мережі нема - сесію не скидаємо
                }

                when {
                    response.status.isSuccess() -> {
                        val body = response.body<RefreshResponse>()
                        tokenStorage.saveTokens(AuthTokens(body.accessToken, body.refreshToken))
                        KermitLogger.i("Auth") { "Tokens refreshed" }
                        BearerTokens(body.accessToken, body.refreshToken)
                    }

                    response.status == HttpStatusCode.Unauthorized ||
                        response.status == HttpStatusCode.Forbidden -> {
                        // "паспорт" теж прострочений -> назад на логін
                        KermitLogger.w("Auth") { "Refresh token expired, logging out" }
                        tokenStorage.clear()
                        sessionEvents.notifyExpired()
                        null
                    }

                    else -> null
                }
            }

            // Токен додаємо одразу до всіх запитів, крім логіну та оновлення
            sendWithoutRequest { request ->
                val path = request.url.encodedPath
                !path.endsWith(LOGIN_PATH) && !path.endsWith(REFRESH_PATH)
            }
        }
    }
}
