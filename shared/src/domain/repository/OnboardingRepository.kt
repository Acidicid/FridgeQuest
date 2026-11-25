package org.example.project.domain.repository

/** Звідки ми знаємо, чи пройдено онбординг. Use case-и знають лише цей інтерфейс. */
interface OnboardingRepository {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted(completed: Boolean)
}
