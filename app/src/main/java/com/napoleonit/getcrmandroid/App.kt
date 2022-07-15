package com.napoleonit.getcrmandroid

import android.app.Application
import com.napoleonit.uxrocket.UXRocket

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //SDK INITIALIZATION SAMPLE
        UXRocket.configure(
            appContext = applicationContext,
            authKey = "2JIJ67L7CS",
            appRocketId = "test_android_sdk_uxrocket"
        )
        UXRocket.setCountryAndCity("Armenia", "Yerevan")
        UXRocket.debugModel(isEnabled = true)
    }
}