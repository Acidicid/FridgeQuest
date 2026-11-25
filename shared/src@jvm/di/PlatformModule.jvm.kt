package org.example.project.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import java.io.File
import org.koin.dsl.module

// На десктопі даємо окрему папку й простір імен, щоб не перетнутися з іншими застосунками на KSafe
actual val platformModule = module {
    single {
        KSafe(
            baseDir = File(System.getProperty("user.home"), ".fridgequest"),
            config = KSafeConfig(appNamespace = "org.example.project.fridgequest"),
        )
    }
}
