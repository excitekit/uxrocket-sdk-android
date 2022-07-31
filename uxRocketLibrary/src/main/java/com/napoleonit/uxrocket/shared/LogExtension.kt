package com.napoleonit.uxrocket.shared

import android.util.Log
import com.napoleonit.uxrocket.UXRocket.isDebugModeEnabled

/**
 *  Extension-ы для того чтобы логи не прошли если разработчик мобильного приложения не включал debugModel
 */

fun Any?.logInfo() {
    if (isDebugModeEnabled)
        Log.i(UXRocketConstants.NAME_SDK, this.toString())
}

fun Any?.logError() {
    if (isDebugModeEnabled)
        Log.e(UXRocketConstants.NAME_SDK, this.toString())
}