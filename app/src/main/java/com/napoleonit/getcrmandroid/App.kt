package com.napoleonit.getcrmandroid

import android.app.Application
import android.os.Build
import com.napoleonit.crmlibrary.UXRocket

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        UXRocket.init(
            appContext = applicationContext,
            authKey = "D3OLWCEXR4",
            deviceModelName = Build.MODEL,
            appRocketId = "test_android_sdk_uxrocket"
        )
    }
}