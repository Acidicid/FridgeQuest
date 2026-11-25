package org.example.project.ui.auth

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.example.project.core.mvi.MviViewModel
import org.example.project.domain.model.AppResult
import org.example.project.domain.usecase.LoginUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.domain.usecase.ValidateUsernameUseCase

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val login: LoginUseCase,
) : MviViewModel<AuthState, AuthIntent, AuthEffect>(AuthState()) {

    override fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UsernameChanged -> setState {
                copy(
                    username = intent.value,
                    usernameError = if (showErrors) validateUsername(intent.value) else null,
                )
            }

            is AuthIntent.PasswordChanged -> setState {
                copy(
                    password = intent.value,
                    passwordError = if (showErrors) validatePassword(intent.value) else null,
                )
            }

            AuthIntent.TogglePasswordVisibility ->
                setState { copy(isPasswordVisible = !isPasswordVisible) }

            AuthIntent.SignInClicked -> submit()
        }
    }

    private fun submit() {
        val s = currentState
        if (s.isLoading) return

        // 1. Спершу локальна перевірка - нема сенсу смикати сервер з порожнім паролем
        val usernameError = validateUsername(s.username)
        val passwordError = validatePassword(s.password)
        setState {
            copy(showErrors = true, usernameError = usernameError, passwordError = passwordError)
        }
        when {
            usernameError != null -> { sendEffect(AuthEffect.FocusUsername); return }
            passwordError != null -> { sendEffect(AuthEffect.FocusPassword); return }
        }

        // 2. Запит на сервер
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            when (val result = login(s.username, s.password)) {
                is AppResult.Success -> {
                    Logger.i("Auth") { "Logged in as ${s.username.trim()}" }
                    sendEffect(AuthEffect.NavigateToHome)
                }
                is AppResult.Error -> {
                    Logger.w("Auth") { "Login failed: ${result.error}" }
                    sendEffect(AuthEffect.ShowError(result.error))
                }
            }
            setState { copy(isLoading = false) }
        }
    }
}
