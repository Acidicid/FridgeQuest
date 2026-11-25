package org.example.project.ui.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.project.core.mvi.MviViewModel
import org.example.project.domain.model.AppResult
import org.example.project.domain.usecase.GetCurrentUserUseCase
import org.example.project.domain.usecase.LogoutUseCase

class HomeViewModel(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val logout: LogoutUseCase,
) : MviViewModel<HomeState, HomeIntent, HomeEffect>(HomeState()) {

    init {
        loadUser()
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.RefreshClicked -> loadUser()
            HomeIntent.LogoutClicked -> doLogout()
        }
    }

    private fun loadUser() {
        if (currentState.isLoading) return
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            when (val result = getCurrentUser()) {
                is AppResult.Success -> setState { copy(user = result.data) }
                is AppResult.Error -> sendEffect(HomeEffect.ShowError(result.error))
            }
            setState { copy(isLoading = false) }
        }
    }

    private fun doLogout() {
        if (currentState.isLoggingOut) return
        viewModelScope.launch {
            setState { copy(isLoggingOut = true) }
            logout()
            sendEffect(HomeEffect.NavigateToAuth)
        }
    }
}
