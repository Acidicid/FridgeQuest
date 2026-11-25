package org.example.project.ui.main

import androidx.lifecycle.ViewModel
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.domain.usecase.IsOnboardingCompletedUseCase
import org.example.project.getPlatform
import org.example.project.navigation.Route

data class MainState(
    /** null = ще перевіряємо, куди відкрити застосунок. */
    val startRoute: Route? = null,
)

/** ViewModel головного (кореневого) екрана: вирішує стартовий екран. */
class MainViewModel(
    private val isOnboardingCompleted: IsOnboardingCompletedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()

    init {
        val done = isOnboardingCompleted()
        Logger.i("Main") { "Started on ${getPlatform().name}, onboardingDone=$done" }
        _state.value = MainState(startRoute = if (done) Route.Auth else Route.Onboarding)
    }
}
