package org.example.project.data.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.example.project.data.auth.model.LoginRequest
import org.example.project.data.auth.model.LoginResponse
import org.example.project.data.auth.model.UserDto
import org.example.project.data.network.BASE_URL
import org.example.project.data.network.LOGIN_PATH
import org.example.project.data.network.ME_PATH
import org.example.project.data.network.defaultHttpError
import org.example.project.data.network.safeRequest
import org.example.project.domain.model.AppError
import org.example.project.domain.model.AppResult

/** Самі HTTP-запити. Нічого не знає про сховище чи UI. */
interface AuthApiService {
    suspend fun login(username: String, password: String): AppResult<LoginResponse>
    suspend fun getMe(): AppResult<UserDto>
    /** Ktor кешує токени в памʼяті - після логіну/виходу кеш треба скинути. */
    suspend fun clearCachedTokens()
}

class KtorAuthApiService(private val client: HttpClient) : AuthApiService {

    override suspend fun login(username: String, password: String): AppResult<LoginResponse> =
        safeRequest(
            // DummyJSON на неправильний логін/пароль відповідає 400
            mapHttpError = { status -> if (status == 400 || status == 401) AppError.InvalidCredentials else defaultHttpError(status) },
        ) {
            client.post("$BASE_URL$LOGIN_PATH") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username = username, password = password))
            }
        }

    override suspend fun getMe(): AppResult<UserDto> =
        safeRequest { client.get("$BASE_URL$ME_PATH") }

    override suspend fun clearCachedTokens() {
        client.authProvider<BearerAuthProvider>()?.clearToken()
    }
}
