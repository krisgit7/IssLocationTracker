package com.example.isslocationtrackerapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class FormatUtils {

    companion object {

        fun getFormatted(double: Double): String {
            return String.format("%.2f", double)
        }

        fun formatToDateString(timestamp: Int): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("EST")
            }
            val date = Date(timestamp.toLong() * 1000)

            return sdf.format(date)
        }
    }
}