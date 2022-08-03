package com.napoleonit.uxrocket.shared

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

const val SERVER_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS"
fun getCurrentDateString(): String = DateTime.now(DateTimeZone.UTC).toString(SERVER_DATE_PATTERN)