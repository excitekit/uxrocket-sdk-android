package com.napoleonit.uxrocket.shared

import android.content.Context
import java.io.IOException

class ReadAssetsUtil(private val appContext: Context) {
    fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = appContext.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}