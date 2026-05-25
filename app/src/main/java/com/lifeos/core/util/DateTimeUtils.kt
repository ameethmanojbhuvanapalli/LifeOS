package com.lifeos.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault())

    fun formatDate(epochMillis: Long): String = formatter.format(Instant.ofEpochMilli(epochMillis))
}
