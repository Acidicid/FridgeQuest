package org.example.project.ui.auth

import org.example.project.domain.model.AppError
import org.example.project.domain.model.PasswordError
import org.example.project.domain.model.UsernameError

data class AuthState(
    val username: String = "",
    val password: String = "",
    val usernameError: UsernameError? = null,
    val passwordError: PasswordError? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    /** Після першої спроби входу помилки перевіряються "наживо" під час введення. */
    val showErrors: Boolean = false,
)

sealed interface AuthIntent {
    data class UsernameChanged(val value: String) : AuthIntent
    data class PasswordChanged(val value: String) : AuthIntent
    data object TogglePasswordVisibility : AuthIntent
    /** І кнопка "Увійти", і дія Done на клавіатурі в полі пароля. */
    data object SignInClicked : AuthIntent
}

sealed interface AuthEffect {
    data object NavigateToHome : AuthEffect
    data object FocusUsername : AuthEffect
    data object FocusPassword : AuthEffect
    /** Разова подія -> спливний снекбар з текстом помилки. */
    data class ShowError(val error: AppError) : AuthEffect
}
