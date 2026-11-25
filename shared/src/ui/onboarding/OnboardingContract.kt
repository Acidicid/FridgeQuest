package org.example.project.ui.onboarding

const val ONBOARDING_PAGE_COUNT = 4

data class OnboardingState(
    val currentPage: Int = 0,
    val pageCount: Int = ONBOARDING_PAGE_COUNT,
    val isFinishing: Boolean = false,
) {
    val isLastPage: Boolean get() = currentPage == pageCount - 1
}

sealed interface OnboardingIntent {
    /** Користувач свайпнув пейджер. */
    data class PageChanged(val page: Int) : OnboardingIntent
    /** Кнопка "Далі" / "Почати квест". */
    data object NextClicked : OnboardingIntent
    data object SkipClicked : OnboardingIntent
}

sealed interface OnboardingEffect {
    data object NavigateToAuth : OnboardingEffect
}
