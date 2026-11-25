package org.example.project.ui.auth

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.example.project.core.mvi.MviViewModel
import org.example.project.domain.usecase.SignInUseCase
import org.example.project.domain.usecase.ValidatePasswordUseCase
import org.example.project.domain.usecase.ValidateUsernameUseCase

class AuthViewModel(
    private val validateUsername: ValidateUsernameUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val signIn: SignInUseCase,
) : MviViewModel<AuthState, AuthIntent, AuthEffect>(AuthState()) {

    override fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.UsernameChanged -> setState {
                copy(
                    username = intent.value,
                    usernameError = if (showErrors) validateUsername(intent.value) else null,
                    signInFailed = false,
                )
            }

            is AuthIntent.PasswordChanged -> setState {
                copy(
                    password = intent.value,
                    passwordError = if (showErrors) validatePassword(intent.value) else null,
                    signInFailed = false,
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

        val usernameError = validateUsername(s.username)
        val passwordError = validatePassword(s.password)
        setState {
            copy(showErrors = true, usernameError = usernameError, passwordError = passwordError)
        }

        // Є помилка - ставимо курсор у перше "погане" поле
        when {
            usernameError != null -> { sendEffect(AuthEffect.FocusUsername); return }
            passwordError != null -> { sendEffect(AuthEffect.FocusPassword); return }
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, signInFailed = false) }
            val username = s.username.trim()
            signIn(username, s.password)
                .onSuccess {
                    Logger.i("Auth") { "Signed in as $username" }
                    sendEffect(AuthEffect.NavigateToHome(username))
                }
                .onFailure { e ->
                    Logger.w("Auth", e) { "Sign in failed" }
                    setState { copy(signInFailed = true) }
                }
            setState { copy(isLoading = false) }
        }
    }
}
