package org.example.project.data

import com.russhwolf.settings.Settings

/**
 * "Пам'ять" додатка: зберігає прапорець, що онбординг уже пройдено.
 * На Android це SharedPreferences, на Desktop - Java Preferences, у браузері - localStorage.
 */
class AppSettings(private val settings: Settings = Settings()) {

    var onboardingDone: Boolean
        get() = settings.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = settings.putBoolean(KEY_ONBOARDING_DONE, value)

    private companion object {
        const val KEY_ONBOARDING_DONE = "onboarding_done"
    }
}
