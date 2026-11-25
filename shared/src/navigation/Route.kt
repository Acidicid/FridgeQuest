package org.example.project.navigation

import androidx.navigation3.runtime.NavKey

/** Усі екрани застосунку. Back stack у Navigation 3 - це просто список таких ключів. */
sealed interface Route : NavKey {
    data object Onboarding : Route
    data object Auth : Route
    data object Home : Route
}
