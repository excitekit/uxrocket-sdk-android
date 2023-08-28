package com.napoleonit.getcrmandroid

import android.app.Application
import com.napoleonit.uxrocket.UXRocket
import com.napoleonit.uxrocket.data.models.http.AttributeParameter
import com.napoleonit.uxrocket.shared.UXRocketServer

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
            appRocketId = "test_android_sdk_uxrocket",
            serverEnvironment = UXRocketServer.develop(),
            true
        )

        UXRocket.setCountryAndCity("Armenia", "Yerevan")
        UXRocket.debugMode(isEnabled = true)

        //SETTER DEFAULTS PARAMS SAMPLE
        UXRocket.setDefaultsParams(
            listOf(
                AttributeParameter("2", "Красный"),
                AttributeParameter("3", "Черный")
            )
        )
    }
}