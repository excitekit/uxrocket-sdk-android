package com.napoleonit.uxrocket.di

import android.content.Context
import com.napoleonit.uxrocket.shared.UXRocketServer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object DI {
    @Synchronized
    fun configure(appContext: Context, authKey: String, appRocketId: String, serverEnvironment: UXRocketServer) {
        startKoin {
            androidContext(appContext)
            modules(listOf(getDataModule(appContext, authKey, appRocketId,serverEnvironment)))
        }
    }
}