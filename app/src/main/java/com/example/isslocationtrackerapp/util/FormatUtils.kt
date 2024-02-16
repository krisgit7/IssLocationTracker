package com.example.isslocationtrackerapp.util

class FormatUtils {

    companion object {

        fun getFormatted(double: Double): String {
            return String.format("%.2f", double)
        }
    }
}