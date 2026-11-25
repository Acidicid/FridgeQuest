package org.example.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.example.project.di.initKoin
import org.example.project.navigation.AppNavigation
import org.example.project.ui.theme.FridgeQuestTheme
import org.jetbrains.compose.reload.DevelopmentEntryPoint

@Composable
@DevelopmentEntryPoint
fun App() {
    // Koin стартує в точці входу кожної платформи (Android - Application, Desktop/Web - main).
    // Тут - лише "страховка" для Compose Hot Reload, який запускає App() без main().
    remember { initKoin() }

    FridgeQuestTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppNavigation()
        }
    }
}
