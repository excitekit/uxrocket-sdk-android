package com.napoleonit.uxrocket.shared

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

const val SERVER_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"


@SuppressLint("SimpleDateFormat")
fun getCurrentDateString() = SimpleDateFormat(SERVER_DATE_PATTERN).format(Calendar.getInstance().time)