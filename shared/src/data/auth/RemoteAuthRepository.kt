package org.example.project.data.auth

import kotlinx.coroutines.flow.Flow
import org.example.project.data.auth.model.toDomain
import org.example.project.data.security.AuthTokens
import org.example.project.data.security.TokenStorage
import org.example.project.data.session.SessionEvents
import org.example.project.domain.model.AppResult
import org.example.project.domain.model.User
import org.example.project.domain.model.map
import org.example.project.domain.repository.AuthRepository

/** Склеює мережу (API) і захищене сховище токенів. */
class RemoteAuthRepository(
    private val api: AuthApiService,
    private val tokenStorage: TokenStorage,
    sessionEvents: SessionEvents,
) : AuthRepository {

    override val sessionExpired: Flow<Unit> = sessionEvents.expired

    override suspend fun login(username: String, password: String): AppResult<Unit> {
        val result = api.login(username, password)
        if (result is AppResult.Success) {
            tokenStorage.saveTokens(AuthTokens(result.data.accessToken, result.data.refreshToken))
            api.clearCachedTokens() // щоб Ktor підхопив нові токени
        }
        return result.map { }
    }

    override suspend fun getCurrentUser(): AppResult<User> =
        api.getMe().map { it.toDomain() }

    override suspend fun logout() {
        tokenStorage.clear()
        api.clearCachedTokens()
    }

    override suspend fun isLoggedIn(): Boolean = tokenStorage.getTokens() != null
}
