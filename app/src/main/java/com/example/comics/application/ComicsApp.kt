package com.example.comics.application

import android.app.Application
import com.example.comics.di.comicsModule
import com.example.comics.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ComicsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ComicsApp)
            modules(comicsModule, networkModule)
        }
    }
}