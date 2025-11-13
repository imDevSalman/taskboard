package com.example.core.common.utils

import com.example.core.common.constants.Constants.DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm aa", Locale.getDefault())

    fun formatDate(timestamp: Long): String = formatter.format(Date(timestamp))

    fun now(): String = formatDate(System.currentTimeMillis())
}
