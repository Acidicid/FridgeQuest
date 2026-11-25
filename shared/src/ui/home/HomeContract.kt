package org.example.project.ui.home

import org.example.project.domain.model.AppError
import org.example.project.domain.model.User

data class HomeState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
)

sealed interface HomeIntent {
    /** Повторити авторизований запит auth/me (зручно, щоб побачити авто-оновлення токена). */
    data object RefreshClicked : HomeIntent
    data object LogoutClicked : HomeIntent
}

sealed interface HomeEffect {
    data object NavigateToAuth : HomeEffect
    data class ShowError(val error: AppError) : HomeEffect
}
