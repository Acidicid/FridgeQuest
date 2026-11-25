package org.example.project.data.security

import eu.anifantakis.lib.ksafe.KSafe

data class AuthTokens(
    val accessToken: String,
    val refreshToken: String,
)

interface TokenStorage {
    suspend fun getTokens(): AuthTokens?
    suspend fun saveTokens(tokens: AuthTokens)
    suspend fun clear()
}

/**
 * Токени лежать у KSafe - він шифрує значення перед записом:
 * Android - Keystore, Desktop - ключ у файлі з правами лише для власника, Web - WebCrypto.
 * Звичайний multiplatform-settings для токенів не годиться: там усе відкритим текстом.
 */
class KSafeTokenStorage(private val ksafe: KSafe) : TokenStorage {

    override suspend fun getTokens(): AuthTokens? {
        val access = ksafe.get(KEY_ACCESS_TOKEN, "")
        val refresh = ksafe.get(KEY_REFRESH_TOKEN, "")
        return if (access.isBlank() || refresh.isBlank()) null else AuthTokens(access, refresh)
    }

    override suspend fun saveTokens(tokens: AuthTokens) {
        ksafe.put(KEY_ACCESS_TOKEN, tokens.accessToken)
        ksafe.put(KEY_REFRESH_TOKEN, tokens.refreshToken)
    }

    override suspend fun clear() {
        ksafe.put(KEY_ACCESS_TOKEN, "")
        ksafe.put(KEY_REFRESH_TOKEN, "")
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
