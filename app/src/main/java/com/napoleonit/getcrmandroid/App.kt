package com.napoleonit.getcrmandroid

import android.app.Application
import com.napoleonit.crmlibrary.GetCrm

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        GetCrm.init(applicationContext)
    }
}