package com.napoleonit.uxrocket.shared

import android.content.res.Resources
import android.util.TypedValue.*
import com.napoleonit.uxrocket.shared.UXRocketConstants.NAME_SDK


fun String.convertToPx(): Float {
    val unit = this.filter { it.isLetter() }
    val size = this.filter { !it.isLetter() }.toFloat()
    return applyDimension(
        getSizeUnit(unit),
        size,
        Resources.getSystem().displayMetrics
    )
}

fun getSizeUnit(unitString: String) = when (unitString) {
    "pt" -> COMPLEX_UNIT_PT
    "px" -> COMPLEX_UNIT_PX
    "dip" -> COMPLEX_UNIT_DIP
    "sp" -> COMPLEX_UNIT_SP
    "in" -> COMPLEX_UNIT_IN
    "mm" -> COMPLEX_UNIT_MM
    else -> throw IllegalArgumentException("$NAME_SDK: Size UNIT not correctly")
}