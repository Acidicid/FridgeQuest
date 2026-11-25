package org.example.project

import android.app.Application
import org.example.project.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

/** Стартує раз на весь процес - тут і запускаємо Koin (KSafe потрібен Context). */
class FridgeQuestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@FridgeQuestApp)
            androidLogger()
        }
    }
}
