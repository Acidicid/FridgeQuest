package org.example.project.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.example.project.domain.usecase.IsOnboardingCompletedUseCase
import org.example.project.domain.usecase.IsUserLoggedInUseCase
import org.example.project.domain.usecase.ObserveSessionExpiredUseCase
import org.example.project.getPlatform
import org.example.project.navigation.Route

data class MainState(
    /** null = ще перевіряємо, куди відкрити застосунок. */
    val startRoute: Route? = null,
)

sealed interface MainEffect {
    /** Токени остаточно протухли - повертаємо на логін. */
    data object SessionExpired : MainEffect
}

/** ViewModel головного (кореневого) екрана: вирішує стартовий екран і стежить за сесією. */
class MainViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
    private val isUserLoggedIn: IsUserLoggedInUseCase,
    observeSessionExpired: ObserveSessionExpiredUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _effects = Channel<MainEffect>(Channel.BUFFERED)
    val effects: Flow<MainEffect> = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            val onboardingDone = isOnboardingCompleted()
            val loggedIn = isUserLoggedIn()
            Logger.i("Main") {
                "Started on ${getPlatform().name}, onboardingDone=$onboardingDone, loggedIn=$loggedIn"
            }
            val route = when {
                !onboardingDone -> Route.Onboarding
                loggedIn -> Route.Home
                else -> Route.Auth
            }
            _state.value = MainState(startRoute = route)
        }

        viewModelScope.launch {
            observeSessionExpired().collect { _effects.send(MainEffect.SessionExpired) }
        }
    }
}
