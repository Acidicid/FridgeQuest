package org.example.project.data.repository

import com.russhwolf.settings.Settings
import org.example.project.domain.repository.OnboardingRepository

/**
 * Реалізація через multiplatform-settings:
 * Android - SharedPreferences, Desktop - Java Preferences, Web - localStorage.
 */
class SettingsOnboardingRepository(private val settings: Settings) : OnboardingRepository {

    override fun isOnboardingCompleted(): Boolean =
        settings.getBoolean(KEY_ONBOARDING_DONE, false)

    override fun setOnboardingCompleted(completed: Boolean) =
        settings.putBoolean(KEY_ONBOARDING_DONE, completed)

    private companion object {
        // той самий ключ, що й у лабі 4, щоб збережений прапорець не "загубився"
        const val KEY_ONBOARDING_DONE = "onboarding_done"
    }
}
