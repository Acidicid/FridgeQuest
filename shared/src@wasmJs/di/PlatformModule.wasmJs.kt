package org.example.project.di

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.dsl.module

// У браузері KSafe шифрує через WebCrypto і зберігає в localStorage
actual val platformModule = module {
    single { KSafe() }
}
