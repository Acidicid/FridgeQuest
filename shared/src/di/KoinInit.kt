package org.example.project.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.mp.KoinPlatform

/** Модуль з тим, що на кожній платформі створюється по-своєму (KSafe). */
expect val platformModule: Module

/**
 * Запуск Koin. Викликається з точки входу кожної платформи
 * (Android - Application, Desktop/Web - main). Повторний виклик нічого не робить.
 */
fun initKoin(config: KoinAppDeclaration = {}) {
    if (KoinPlatform.getKoinOrNull() != null) return
    startKoin {
        config()
        modules(appModules)
    }
}
