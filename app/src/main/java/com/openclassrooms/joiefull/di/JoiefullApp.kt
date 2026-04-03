package com.openclassrooms.joiefull.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JoiefullApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@JoiefullApp)
            modules(appModule)
        }
    }
}