package org.example.project.ui.onboarding

import co.touchlab.kermit.Logger
import org.example.project.core.mvi.MviViewModel
import org.example.project.domain.usecase.CompleteOnboardingUseCase

class OnboardingViewModel(
    private val completeOnboarding: CompleteOnboardingUseCase,
) : MviViewModel<OnboardingState, OnboardingIntent, OnboardingEffect>(OnboardingState()) {

    override fun onIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.PageChanged ->
                setState { copy(currentPage = intent.page.coerceIn(0, pageCount - 1)) }

            OnboardingIntent.NextClicked ->
                if (currentState.isLastPage) finish("Finished")
                else setState { copy(currentPage = currentPage + 1) }

            OnboardingIntent.SkipClicked ->
                finish("Skipped on page ${currentState.currentPage + 1}")
        }
    }

    private fun finish(reason: String) {
        if (currentState.isFinishing) return // захист від подвійного натискання
        setState { copy(isFinishing = true) }
        Logger.i("Onboarding") { reason }
        completeOnboarding()
        sendEffect(OnboardingEffect.NavigateToAuth)
    }
}
