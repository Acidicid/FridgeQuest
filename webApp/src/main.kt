package org.example.project

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import eu.anifantakis.lib.ksafe.KSafe
import org.example.project.di.initKoin
import org.koin.mp.KoinPlatform

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport {
        // У браузері шифрування (WebCrypto) лише асинхронне: поки KSafe не підготував кеш,
        // читання повертало б порожні токени. Тому чекаємо, а вже потім малюємо застосунок.
        var storageReady by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            KoinPlatform.getKoin().get<KSafe>().awaitCacheReady()
            storageReady = true
        }
        if (storageReady) App()
    }
}
