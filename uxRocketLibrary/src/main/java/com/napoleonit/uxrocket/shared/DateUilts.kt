package com.napoleonit.uxrocket.shared

import android.annotation.SuppressLint
import android.os.Build
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

const val SERVER_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"


@SuppressLint("SimpleDateFormat")
fun getCurrentDateString() = DateTime.now(DateTimeZone.UTC).toString(SERVER_DATE_PATTERN)