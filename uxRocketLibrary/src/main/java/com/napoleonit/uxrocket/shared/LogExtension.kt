package com.napoleonit.uxrocket.shared

import android.util.Log
import com.napoleonit.uxrocket.UXRocket.isDebugModeEnabled


fun Any?.logInfo() {
    if (isDebugModeEnabled)
        Log.i(UXRocketConstants.NAME_SDK, this.toString())
}

fun Any?.logError() {
    if (isDebugModeEnabled)
        Log.e(UXRocketConstants.NAME_SDK, this.toString())
}