package org.example.project.domain.usecase

import org.example.project.domain.repository.OnboardingRepository

/** Зберегти результат: онбординг пройдено. */
class CompleteOnboardingUseCase(private val repository: OnboardingRepository) {
    operator fun invoke() = repository.setOnboardingCompleted(true)
}

/** Перевірити, чи користувач уже проходив онбординг. */
class IsOnboardingCompletedUseCase(private val repository: OnboardingRepository) {
    operator fun invoke(): Boolean = repository.isOnboardingCompleted()
}
