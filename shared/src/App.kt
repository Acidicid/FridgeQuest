package org.example.project

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import org.example.project.data.AppSettings
import org.example.project.resources.Res
import org.example.project.resources.home_placeholder
import org.example.project.ui.onboarding.OnboardingScreen
import org.example.project.ui.theme.FridgeQuestTheme
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    FridgeQuestTheme {
        val appSettings = remember { AppSettings() }
        var showOnboarding by remember { mutableStateOf(!appSettings.onboardingDone) }

        LaunchedEffect(Unit) {
            Logger.i("App") { "Started on ${getPlatform().name}, showOnboarding=$showOnboarding" }
        }

        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            if (showOnboarding) {
                OnboardingScreen(onFinish = {
                    appSettings.onboardingDone = true
                    showOnboarding = false
                })
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(Res.string.home_placeholder),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
