package org.example.project.data.network

/** Тестовий бекенд з авторизацією: https://dummyjson.com/docs/auth */
internal const val BASE_URL = "https://dummyjson.com"

internal const val LOGIN_PATH = "/auth/login"
internal const val REFRESH_PATH = "/auth/refresh"
internal const val ME_PATH = "/auth/me"

/**
 * Скільки хвилин живе access-токен.
 * Навмисно 1 хвилина: зачекай хвилину на головному екрані, натисни "Оновити профіль"
 * і в логах побачиш, як Ktor сам сходив на /auth/refresh.
 */
internal const val ACCESS_TOKEN_LIFETIME_MINS = 1
