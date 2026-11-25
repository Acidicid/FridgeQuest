package org.example.project.di

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// На Android KSafe потрібен Context (ключі шифрування живуть в Android Keystore)
actual val platformModule = module {
    single { KSafe(androidContext()) }
}
