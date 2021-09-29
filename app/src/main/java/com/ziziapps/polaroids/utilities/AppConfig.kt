package com.ziziapps.polaroids.utilities

import android.app.Application
import com.ziziapps.polaroids.depenedencyInjection.repositoryModule
import com.ziziapps.polaroids.depenedencyInjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig: Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}