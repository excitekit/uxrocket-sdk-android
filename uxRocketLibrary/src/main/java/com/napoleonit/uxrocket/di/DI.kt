package com.napoleonit.uxrocket.di

import android.content.Context
import android.util.Log
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication

object DI {
    @Synchronized
    fun configure(appContext: Context, authKey: String, appRocketId: String) {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(appContext)
            modules(listOf(getDataModule(appContext, authKey, appRocketId)))
        }
    }
}