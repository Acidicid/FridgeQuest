package org.example.project.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import org.example.project.ui.auth.AuthScreen
import org.example.project.ui.home.HomeScreen
import org.example.project.ui.main.MainEffect
import org.example.project.ui.main.MainViewModel
import org.example.project.ui.onboarding.OnboardingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(mainViewModel: MainViewModel = koinViewModel()) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()

    // Поки MainViewModel не вирішила, куди йти, показуємо лоадер
    val startRoute = state.startRoute
    if (startRoute == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val backStack = remember(startRoute) { mutableStateListOf<Route>(startRoute) }

    // Сесія протухла (не вдалося оновити токен) -> на логін, звідки б ми не були
    LaunchedEffect(mainViewModel) {
        mainViewModel.effects.collect { effect ->
            when (effect) {
                MainEffect.SessionExpired ->
                    if (backStack.lastOrNull() != Route.Auth) backStack.resetTo(Route.Auth)
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<Route>(),
            // кожен екран отримує власний ViewModelStore -> свою ViewModel
            rememberViewModelStoreNavEntryDecorator<Route>(),
        ),
        entryProvider = entryProvider<Route> {
            entry<Route.Onboarding> {
                OnboardingScreen(onFinished = { backStack.resetTo(Route.Auth) })
            }
            entry<Route.Auth> {
                AuthScreen(onLoggedIn = { backStack.resetTo(Route.Home) })
            }
            entry<Route.Home> {
                HomeScreen(onLoggedOut = { backStack.resetTo(Route.Auth) })
            }
        },
    )
}

/** Замінити весь стек одним екраном, щоб "Назад" не повертав на онбординг/логін. */
private fun SnapshotStateList<Route>.resetTo(route: Route) {
    add(route)
    subList(0, lastIndex).clear()
}
