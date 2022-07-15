package com.napoleonit.uxrocket.di

import android.content.Context
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

object DI {
    private lateinit var diApp: KoinApplication

    @ExperimentalSerializationApi
    fun configure(appContext: Context, authKey: String, appRocketId: String) {
        diApp = startKoin {
            androidContext(appContext)
            modules(getDataModule(appContext, authKey, appRocketId))
        }
    }
}