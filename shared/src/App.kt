package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.example.project.di.appModules
import org.example.project.navigation.AppNavigation
import org.example.project.ui.theme.FridgeQuestTheme
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    // Запускаємо Koin один раз для всіх платформ (Android, Desktop, Web)
    KoinApplication(configuration = koinConfiguration { modules(appModules) }) {
        FridgeQuestTheme {
            Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                AppNavigation()
            }
        }
    }
}
